package edu.ecnu.sei.group08.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import edu.ecnu.sei.group08.common.LocalUser;
import edu.ecnu.sei.group08.common.mybatis.Page;
import edu.ecnu.sei.group08.dto.user.ChangePasswordDTO;
import edu.ecnu.sei.group08.mapper.UserRoleMapper;
import edu.ecnu.sei.group08.model.UserRoleDO;
import edu.ecnu.sei.group08.service.PermissionService;
import edu.ecnu.sei.group08.service.UserIdentityService;
import edu.ecnu.sei.group08.dto.user.RegisterDTO;
import edu.ecnu.sei.group08.dto.user.UpdateInfoDTO;
import edu.ecnu.sei.group08.mapper.UserMapper;
import edu.ecnu.sei.group08.model.RoleDO;
import edu.ecnu.sei.group08.model.PermissionDO;
import edu.ecnu.sei.group08.model.UserDO;
import edu.ecnu.sei.group08.service.RoleService;
import edu.ecnu.sei.group08.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.talelin.autoconfigure.exception.FailedException;
import io.github.talelin.autoconfigure.exception.ForbiddenException;
import io.github.talelin.autoconfigure.exception.NotFoundException;
import io.github.talelin.autoconfigure.exception.ParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    @Autowired
    private UserIdentityService userIdentityService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserRoleMapper userGroupMapper;

    @Value("${group.root.id}")
    private Long rootRoleId;

    @Value("${group.guest.id}")
    private Long guestRoleId;

    @Transactional
    @Override
    public UserDO createUser(RegisterDTO dto) {
        boolean exist = this.checkUserExistByUsername(dto.getUsername());
        if (exist) {
            throw new ForbiddenException("username already exist, please choose a new one", 10071);
        }
        if (StrUtil.isNotBlank(dto.getEmail())) {
            exist = this.checkUserExistByEmail(dto.getEmail());
            if (exist) {
                throw new ForbiddenException("email already exist, please choose a new one", 10076);
            }
        } else {
            // bug 前端如果传入的email为 "" 时，由于数据库中存在""的email，会报duplication错误
            // 所以如果email为blank，必须显示设置为 null
            dto.setEmail(null);
        }
        UserDO user = new UserDO();
        BeanUtil.copyProperties(dto, user);
        this.baseMapper.insert(user);
        if (dto.getRoleIds() != null && !dto.getRoleIds().isEmpty()) {
            checkGroupsValid(dto.getRoleIds());
            checkGroupsExist(dto.getRoleIds());
            List<UserRoleDO> relations = dto.getRoleIds()
                    .stream()
                    .map(roleId -> new UserRoleDO(user.getId(), roleId))
                    .collect(Collectors.toList());
            userGroupMapper.insertBatch(relations);
        } else {
            // id为2的分组为游客分组
            UserRoleDO relation = new UserRoleDO(user.getId(), guestRoleId);
            userGroupMapper.insert(relation);
        }
        userIdentityService.createUsernamePasswordIdentity(user.getId(), dto.getUsername(), dto.getPassword());
        return user;
    }

    @Transactional
    @Override
    public UserDO updateUserInfo(UpdateInfoDTO dto) {
        UserDO user = LocalUser.getLocalUser();
        if (StrUtil.isNotBlank(dto.getUsername())) {
            boolean exist = this.checkUserExistByUsername(dto.getUsername());
            if (exist) {
                throw new ForbiddenException("username already exist, please choose a new one", 10071);
            }
            user.setUsername(dto.getUsername());
            userIdentityService.changeUsername(user.getId(), dto.getUsername());
        }
        BeanUtil.copyProperties(dto, user);
        this.baseMapper.updateById(user);
        return user;
    }

    @Override
    public UserDO changeUserPassword(ChangePasswordDTO dto) {
        UserDO user = LocalUser.getLocalUser();
        boolean valid = userIdentityService.verifyUsernamePassword(user.getId(), user.getUsername(), dto.getOldPassword());
        if (!valid) {
            throw new ParameterException("password invalid, please enter correct password", 10032);
        }
        valid = userIdentityService.changePassword(user.getId(), dto.getNewPassword());
        if (!valid) {
            throw new FailedException("password change failed", 10011);
        }
        return user;
    }

    @Override
    public List<RoleDO> getUserGroups(Long userId) {
        return roleService.getUserGroupsByUserId(userId);
    }

    @Override
    public List<Map<String, List<Map<String, String>>>> getStructualUserPermissions(Long userId) {
        List<PermissionDO> permissions = getUserPermissions(userId);
        return permissionService.structuringPermissions(permissions);
    }

    @Override
    public List<PermissionDO> getUserPermissions(Long userId) {
        // 查找用户搜索分组，查找分组下的所有权限
        List<Long> roleIds = roleService.getUserRoleIdsByUserId(userId);
        if (roleIds == null || roleIds.size() == 0) {
            return new ArrayList<>();
        }
        return permissionService.getPermissionByRoleIds(roleIds);
    }

    @Override
    public UserDO getUserByUsername(String username) {
        QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserDO::getUsername, username);
        return this.getOne(wrapper);
    }

    @Override
    public boolean checkUserExistByUsername(String username) {
        int rows = this.baseMapper.selectCountByUsername(username);
        return rows > 0;
    }

    @Override
    public boolean checkUserExistByEmail(String email) {
        QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserDO::getEmail, email);
        int rows = this.baseMapper.selectCount(wrapper);
        return rows > 0;
    }

    @Override
    public boolean checkUserExistById(Long id) {
        int rows = this.baseMapper.selectCountById(id);
        return rows > 0;
    }

    @Override
    public IPage<UserDO> getUserPageByRoleId(Page pager, Long roleId) {
        return this.baseMapper.selectPageByRoleId(pager, roleId);
    }

    private void checkGroupsExist(List<Long> ids) {
        for (long id : ids) {
            if (!roleService.checkGroupExistById(id)) {
                throw new NotFoundException("group not found，can't create user", 10023);
            }
        }
    }

    private void checkGroupsValid(List<Long> ids) {
        boolean anyMatch = ids.stream().anyMatch(it -> it.equals(rootRoleId));
        if (anyMatch) {
            throw new ForbiddenException("you can't add user to root role", 10073);
        }
    }
}
