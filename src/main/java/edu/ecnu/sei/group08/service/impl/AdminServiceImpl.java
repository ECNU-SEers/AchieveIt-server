package edu.ecnu.sei.group08.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import edu.ecnu.sei.group08.bo.GroupPermissionsBO;
import edu.ecnu.sei.group08.common.mybatis.Page;
import edu.ecnu.sei.group08.dto.admin.*;
import edu.ecnu.sei.group08.mapper.RolePermissionMapper;
import edu.ecnu.sei.group08.model.*;
import edu.ecnu.sei.group08.service.*;
import io.github.talelin.autoconfigure.exception.ForbiddenException;
import io.github.talelin.autoconfigure.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserIdentityService userIdentityService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Value("${group.root.id}")
    private Long rootRoleId;

    @Value("${group.guest.id}")
    private Long guestRoleId;

    @Override
    public IPage<UserDO> getUserPageByRoleId(Long roleId, Long count, Long page) {
        Page pager = new Page(page, count);
        IPage<UserDO> iPage;
        // 如果role_id为空，则以分页的形式返回所有用户
        if (roleId == null) {
            iPage = userService.page(pager);
        } else {
            iPage = userService.getUserPageByRoleId(pager, roleId);
        }
        return iPage;
    }

    @Override
    public boolean changeUserPassword(Long id, ResetPasswordDTO dto) {
        throwUserNotExistById(id);
        return userIdentityService.changePassword(id, dto.getNewPassword());
    }

    @Transactional
    @Override
    public boolean deleteUser(Long id) {
        throwUserNotExistById(id);
        boolean userRemoved = userService.removeById(id);
        QueryWrapper<UserIdentityDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserIdentityDO::getUserId, id);
        return userRemoved && userIdentityService.remove(wrapper);
    }

    @Override
    public boolean updateUserInfo(Long id, UpdateUserInfoDTO validator) {
        List<Long> newRoleIds = validator.getRoleIds();
        if (newRoleIds == null || newRoleIds.isEmpty()) {
            return false;
        }
        boolean anyMatch = newRoleIds.stream().anyMatch(it -> it.equals(rootRoleId));
        if (anyMatch) {
            throw new ForbiddenException("you can't add user to root group", 10073);
        }
        List<Long> existRoleIds = roleService.getUserRoleIdsByUserId(id);
        // 删除existRoleIds有，而newRoleIds没有的
        List<Long> deleteIds = existRoleIds.stream().filter(it -> !newRoleIds.contains(it)).collect(Collectors.toList());
        // 添加newRoleIds有，而existRoleIds没有的
        List<Long> addIds = newRoleIds.stream().filter(it -> !existRoleIds.contains(it)).collect(Collectors.toList());
        return roleService.deleteUserGroupRelations(id, deleteIds) && roleService.addUserGroupRelations(id, addIds);
    }

    @Override
    public IPage<RoleDO> getGroupPage(Long page, Long count) {
        IPage<RoleDO> iPage = roleService.getGroupPage(page, count);
        return iPage;
    }

    @Override
    public GroupPermissionsBO getGroup(Long id) {
        throwGroupNotExistById(id);
        return roleService.getGroupAndPermissions(id);
    }

    @Transactional
    @Override
    public boolean createGroup(NewGroupDTO dto) {
        throwGroupNameExist(dto.getName());
        RoleDO group = RoleDO.builder().name(dto.getName()).info(dto.getInfo()).build();
        roleService.save(group);
        if (dto.getPermissionIds() != null && !dto.getPermissionIds().isEmpty()) {
            List<RolePermissionDO> relations = dto.getPermissionIds().stream()
                    .map(id -> new RolePermissionDO(group.getId(), id))
                    .collect(Collectors.toList());
            rolePermissionMapper.insertBatch(relations);
        }
        return true;
    }

    @Override
    public boolean updateGroup(Long id, UpdateGroupDTO dto) {
        // bug 如果只修改info，不修改name，则name已经存在，此时不应该报错
        RoleDO exist = roleService.getById(id);
        if (exist == null) {
            throw new NotFoundException("group not found", 10024);
        }
        if (!exist.getName().equals(dto.getName())) {
            throwGroupNameExist(dto.getName());
        }
        RoleDO group = RoleDO.builder().id(id).name(dto.getName()).info(dto.getInfo()).build();
        return roleService.updateById(group);
    }

    @Override
    public boolean deleteGroup(Long id) {
        if (id.equals(rootRoleId)) {
            throw new ForbiddenException("root group can't delete", 10074);
        }
        if (id.equals(guestRoleId)) {
            throw new ForbiddenException("guest group can't delete", 10075);
        }
        throwGroupNotExistById(id);
        return roleService.removeById(id);
    }

    @Override
    public boolean dispatchPermission(DispatchPermissionDTO dto) {
        RolePermissionDO groupPermission = new RolePermissionDO(dto.getRoleId(), dto.getPermissionId());
        return rolePermissionMapper.insert(groupPermission) > 0;
    }

    @Override
    public boolean dispatchPermissions(DispatchPermissionsDTO dto) {
        List<RolePermissionDO> relations = dto.getPermissionIds().stream()
                .map(id -> new RolePermissionDO(dto.getRoleId(), id))
                .collect(Collectors.toList());
        return rolePermissionMapper.insertBatch(relations) > 0;
    }

    @Override
    public boolean removePermissions(RemovePermissionsDTO dto) {
        return rolePermissionMapper.deleteBatchByRoleIdAndPermissionId(dto.getRoleId(), dto.getPermissionIds()) > 0;
    }

    @Override
    public List<RoleDO> getAllGroups() {
        return roleService.list();
    }

    @Override
    public List<PermissionDO> getAllPermissions() {
        return permissionService.list();
    }

    @Override
    public Map<String, List<PermissionDO>> getAllStructualPermissions() {
        List<PermissionDO> permissions = permissionService.list();
        Map<String, List<PermissionDO>> res = new HashMap<>();
        permissions.forEach(permission -> {
            if (res.containsKey(permission.getModule())) {
                res.get(permission.getModule()).add(permission);
            } else {
                ArrayList t = new ArrayList();
                t.add(permission);
                res.put(permission.getModule(), t);
            }
        });
        return res;
    }

    private void throwUserNotExistById(Long id) {
        boolean exist = userService.checkUserExistById(id);
        if (!exist) {
            throw new NotFoundException("user not found", 10021);
        }
    }

    private void throwGroupNotExistById(Long id) {
        boolean exist = roleService.checkGroupExistById(id);
        if (!exist) {
            throw new NotFoundException("group not found", 10024);
        }
    }

    private void throwGroupNameExist(String name) {
        boolean exist = roleService.checkGroupExistByName(name);
        if (exist) {
            throw new ForbiddenException("group name already exist, please enter a new one", 10072);
        }
    }
}
