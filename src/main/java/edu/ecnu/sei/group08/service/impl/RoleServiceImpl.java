package edu.ecnu.sei.group08.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import edu.ecnu.sei.group08.bo.GroupPermissionsBO;
import edu.ecnu.sei.group08.common.mybatis.Page;
import edu.ecnu.sei.group08.mapper.RoleMapper;
import edu.ecnu.sei.group08.mapper.UserRoleMapper;
import edu.ecnu.sei.group08.model.UserRoleDO;
import edu.ecnu.sei.group08.service.PermissionService;
import edu.ecnu.sei.group08.model.RoleDO;
import edu.ecnu.sei.group08.model.PermissionDO;
import edu.ecnu.sei.group08.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.talelin.autoconfigure.exception.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleDO> implements RoleService {

    @Value("${group.root.name}")
    private String rootGroupName;

    @Value("${group.root.id}")
    private Long rootRoleId;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserRoleMapper userGroupMapper;

    @Override
    public List<RoleDO> getUserGroupsByUserId(Long userId) {
        return this.baseMapper.selectUserRoles(userId);
    }

    @Override
    public List<Long> getUserRoleIdsByUserId(Long userId) {
        return this.baseMapper.selectUserRoleIds(userId);
    }

    @Override
    public IPage<RoleDO> getGroupPage(long page, long count) {
        Page pager = new Page(page, count);
        return this.baseMapper.selectPage(pager, null);
    }

    @Override
    public boolean checkGroupExistById(Long id) {
        return this.baseMapper.selectCountById(id) > 0;
    }

    @Override
    public GroupPermissionsBO getGroupAndPermissions(Long id) {
        RoleDO group = this.baseMapper.selectById(id);
        List<PermissionDO> permissions = permissionService.getPermissionByRoleId(id);
        return new GroupPermissionsBO(group, permissions);
    }

    @Override
    public boolean checkGroupExistByName(String name) {
        QueryWrapper<RoleDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(RoleDO::getName, name);
        return this.baseMapper.selectCount(wrapper) > 0;
    }

    @Override
    public boolean checkIsRootByUserId(Long userId) {
        QueryWrapper<UserRoleDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserRoleDO::getUserId, userId)
                .eq(UserRoleDO::getRoleId, rootRoleId);
        UserRoleDO relation = userGroupMapper.selectOne(wrapper);
        return relation != null;
    }

    @Override
    public boolean deleteUserGroupRelations(Long userId, List<Long> deleteIds) {
        if (deleteIds == null || deleteIds.isEmpty())
            return true;
        QueryWrapper<UserRoleDO> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(UserRoleDO::getUserId, userId)
                .in(UserRoleDO::getRoleId, deleteIds);
        return userGroupMapper.delete(wrapper) > 0;
    }

    @Override
    public boolean addUserGroupRelations(Long userId, List<Long> addIds) {
        if (addIds == null || addIds.isEmpty())
            return true;
        boolean ok = checkGroupExistByIds(addIds);
        if (!ok) {
            throw new ForbiddenException("cant't add user to non-existent group", 10077);
        }
        List<UserRoleDO> relations = addIds.stream().map(it -> new UserRoleDO(userId, it)).collect(Collectors.toList());
        return userGroupMapper.insertBatch(relations) > 0;
    }

    private boolean checkGroupExistByIds(List<Long> ids) {
        return ids.stream().allMatch(this::checkGroupExistById);
    }
}
