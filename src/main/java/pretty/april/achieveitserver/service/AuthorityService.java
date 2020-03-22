package pretty.april.achieveitserver.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pretty.april.achieveitserver.converter.AuthorityConverter;
import pretty.april.achieveitserver.dto.PageDTO;
import pretty.april.achieveitserver.dto.PermissionDTO;
import pretty.april.achieveitserver.dto.RoleDTO;
import pretty.april.achieveitserver.entity.Permission;
import pretty.april.achieveitserver.entity.Role;
import pretty.april.achieveitserver.entity.RolePermission;
import pretty.april.achieveitserver.entity.UserRole;
import pretty.april.achieveitserver.mapper.PermissionMapper;
import pretty.april.achieveitserver.mapper.RoleMapper;
import pretty.april.achieveitserver.mapper.RolePermissionMapper;
import pretty.april.achieveitserver.mapper.UserRoleMapper;
import pretty.april.achieveitserver.request.AssignRoleRequest;
import pretty.april.achieveitserver.request.CreateRoleRequest;
import pretty.april.achieveitserver.request.EditRoleRequest;
import pretty.april.achieveitserver.request.RevokeRoleRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorityService {

    private PermissionMapper permissionMapper;

    private RoleMapper roleMapper;

    private RolePermissionMapper rolePermissionMapper;

    private UserRoleMapper userRoleMapper;

    private AuthorityConverter authorityConverter;

    public AuthorityService(PermissionMapper permissionMapper, RoleMapper roleMapper, RolePermissionMapper rolePermissionMapper, UserRoleMapper userRoleMapper, AuthorityConverter authorityConverter) {
        this.permissionMapper = permissionMapper;
        this.roleMapper = roleMapper;
        this.rolePermissionMapper = rolePermissionMapper;
        this.userRoleMapper = userRoleMapper;
        this.authorityConverter = authorityConverter;
    }

    public List<PermissionDTO> getPermissions(Integer pageNo, Integer pageSize, Integer total) {
        Page<Permission> page = new Page<>(pageNo, pageSize, total);
        IPage<Permission> permissionIPage = permissionMapper.selectPage(page, new QueryWrapper<>());
        return authorityConverter.toPermissionDTOList(permissionIPage.getRecords());
    }

    public PageDTO<RoleDTO> getRoles(Integer pageNo, Integer pageSize, Integer creatorId) {
        Page<Role> page = new Page<>(pageNo, pageSize);
        IPage<Role> roleIPage = roleMapper.selectPage(page, new QueryWrapper<Role>()
                .eq(true, "creator_id", creatorId)
                .or()
                .eq(true, "creator_id", 1)); // 1 -> admin id
        return new PageDTO<>(roleIPage.getCurrent(), roleIPage.getSize(), roleIPage.getTotal(),
                authorityConverter.toRoleDTOList(roleIPage.getRecords()));
    }

    public Integer createRole(CreateRoleRequest roleRequest, Integer creatorId) {
        Role role = new Role();
        role.setName(roleRequest.getName());
        role.setRemark(roleRequest.getRemark());
        role.setCreatorId(creatorId);
        roleMapper.insert(role);
        if (!CollectionUtils.isEmpty(roleRequest.getPermissions())) {
            List<Permission> permissionList = permissionMapper.selectList(
                    new QueryWrapper<Permission>().in(true, "id", roleRequest.getPermissions()));
            if (permissionList.size() != roleRequest.getPermissions().size()) {
                throw new IllegalArgumentException("Permission not exists");
            }

            List<RolePermission> rps = roleRequest.getPermissions().stream().map(o -> new RolePermission(role.getId(), o))
                    .collect(Collectors.toList());
            rolePermissionMapper.insertBatch(rps);
        }
        return role.getId();
    }

    public void editRole(EditRoleRequest roleRequest, Integer roleId) {
        Role r = roleMapper.selectById(roleId);
        if (r == null) {
            throw new IllegalArgumentException("Role not exists");
        }
        Role role = new Role();
        role.setName(roleRequest.getName());
        role.setRemark(roleRequest.getRemark());
        role.setId(roleId);
        roleMapper.updateById(role);
        if (!CollectionUtils.isEmpty(roleRequest.getPermissions())) {
            List<Permission> permissionList = permissionMapper.selectList(
                    new QueryWrapper<Permission>().in(true, "id", roleRequest.getPermissions()));
            if (permissionList.size() != roleRequest.getPermissions().size()) {
                throw new IllegalArgumentException("Permission not exists");
            }

            List<RolePermission> oldRps = rolePermissionMapper.selectList(
                    new QueryWrapper<RolePermission>().eq(true, "role_id", roleId));
            rolePermissionMapper.deleteBatchIds(oldRps);

            List<RolePermission> newRps = roleRequest.getPermissions().stream().map(o -> new RolePermission(role.getId(), o))
                    .collect(Collectors.toList());
            rolePermissionMapper.insertBatch(newRps);
        }
    }

    public void assignRole(AssignRoleRequest request, Integer roleId) {
        Role r = roleMapper.selectById(roleId);
        if (r == null) {
            throw new IllegalArgumentException("Role not exists");
        }
        UserRole ur = userRoleMapper.selectOne(new QueryWrapper<UserRole>()
                .eq(true, "user_id", request.getAssigneeId())
                .eq(true, "role_id", roleId));
        if (ur != null) {
            return;
        }
        UserRole userRole = new UserRole();
        userRole.setRoleId(roleId);
        userRole.setUserId(request.getAssigneeId());
        userRoleMapper.insert(userRole);
    }

    public void revokeRole(RevokeRoleRequest request, Integer roleId) {
        Role r = roleMapper.selectById(roleId);
        if (r == null) {
            throw new IllegalArgumentException("Role not exists");
        }
        UserRole ur = userRoleMapper.selectOne(new QueryWrapper<UserRole>()
                .eq(true, "user_id", request.getUserId())
                .eq(true, "role_id", roleId));
        if (ur == null) {
            return;
        }
        userRoleMapper.delete(new QueryWrapper<UserRole>()
                .eq(true, "user_id", request.getUserId())
                .eq(true, "role_id", roleId));
    }
}