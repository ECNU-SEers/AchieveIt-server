package pretty.april.achieveitserver.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pretty.april.achieveitserver.converter.AuthorityConverter;
import pretty.april.achieveitserver.dto.*;
import pretty.april.achieveitserver.entity.Permission;
import pretty.april.achieveitserver.entity.Role;
import pretty.april.achieveitserver.entity.RolePermission;
import pretty.april.achieveitserver.entity.UserRole;
import pretty.april.achieveitserver.mapper.PermissionMapper;
import pretty.april.achieveitserver.mapper.RoleMapper;
import pretty.april.achieveitserver.mapper.RolePermissionMapper;
import pretty.april.achieveitserver.mapper.UserRoleMapper;
import pretty.april.achieveitserver.request.AssignRevokeRoleRequest;
import pretty.april.achieveitserver.request.CreateRoleRequest;
import pretty.april.achieveitserver.request.EditRoleRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorityService {

    private PermissionMapper permissionMapper;

    private RoleMapper roleMapper;

    private RolePermissionMapper rolePermissionMapper;

    private UserRoleMapper userRoleMapper;

    private AuthorityConverter authorityConverter;

    private UserService userService;

    public AuthorityService(PermissionMapper permissionMapper, RoleMapper roleMapper, RolePermissionMapper rolePermissionMapper, UserRoleMapper userRoleMapper, AuthorityConverter authorityConverter, UserService userService) {
        this.permissionMapper = permissionMapper;
        this.roleMapper = roleMapper;
        this.rolePermissionMapper = rolePermissionMapper;
        this.userRoleMapper = userRoleMapper;
        this.authorityConverter = authorityConverter;
        this.userService = userService;
    }

    public List<PermissionDTO> getPermissions(Integer pageNo, Integer pageSize) {
        Page<Permission> page = new Page<>(pageNo, pageSize);
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

            rolePermissionMapper.delete(new QueryWrapper<RolePermission>().eq("role_id", roleId));

            List<RolePermission> newRps = roleRequest.getPermissions().stream().map(o -> new RolePermission(role.getId(), o))
                    .collect(Collectors.toList());
            rolePermissionMapper.insertBatch(newRps);
        }
    }

    public void assignRole(AssignRevokeRoleRequest request, Integer roleId, Integer projectId) {
        Role r = roleMapper.selectById(roleId);
        if (r == null) {
            throw new IllegalArgumentException("Role not exists");
        }
        UserRole ur = userRoleMapper.selectOne(new QueryWrapper<UserRole>()
                .eq("user_id", request.getAssigneeId())
                .eq("role_id", roleId)
                .eq("project_id", projectId));
        if (ur != null) {
            return;
        }
        UserRole userRole = new UserRole();
        userRole.setRoleId(roleId);
        userRole.setUserId(request.getAssigneeId());
        userRole.setProjectId(projectId);
        userRoleMapper.insert(userRole);
    }

    public void revokeRole(AssignRevokeRoleRequest request, Integer roleId, Integer projectId) {
        Role r = roleMapper.selectById(roleId);
        if (r == null) {
            throw new IllegalArgumentException("Role not exists");
        }
        UserRole ur = userRoleMapper.selectOne(new QueryWrapper<UserRole>()
                .eq(true, "user_id", request.getAssigneeId())
                .eq(true, "role_id", roleId)
                .eq(true, "project_id", projectId));
        if (ur == null) {
            return;
        }
        userRoleMapper.delete(new QueryWrapper<UserRole>()
                .eq(true, "user_id", request.getAssigneeId())
                .eq(true, "role_id", roleId)
                .eq(true, "project_id", projectId));
    }

    public List<PermissionDTO> getMyPermissions(Integer projectId, Integer userId) {
        List<Permission> permissions = permissionMapper.getByUserIdAndProjectId(userId, projectId);
        return permissions.stream()
                .map(o -> new PermissionDTO(o.getId(), o.getName(), o.getModule(), o.getRemark()))
                .collect(Collectors.toList());
    }

    public List<RoleDTO> getMyRoles(Integer projectId, Integer userId) {
        List<Role> roles = userRoleMapper.getByProjectIdAndUserId(projectId, userId);
        return roles.stream().map(o -> new RoleDTO(o.getId(), o.getName(), o.getRemark())).collect(Collectors.toList());
    }

    public List<PermissionDTO> getRolePermissions(Integer roleId) {
        List<Permission> modulePermissions = permissionMapper.getByRoleId(roleId);
        return modulePermissions.stream()
                .map(o -> new PermissionDTO(o.getId(), o.getName(), o.getModule(), o.getRemark()))
                .collect(Collectors.toList());
    }

    public PageDTO<DetailedProjectRoleDTO> getDetailedRoles(Integer pageNo, Integer pageSize, Integer creatorId, String creatorUsername) {
        Page<Role> page = new Page<>(pageNo, pageSize);
        IPage<Role> roleIPage = roleMapper.selectPage(page, new QueryWrapper<Role>()
                .eq("creator_id", creatorId).or().eq("creator_id", 1));
        List<Role> roles = roleIPage.getRecords();
        List<DetailedProjectRoleDTO> detailedProjectRoleDTOS = new ArrayList<>();
        for (Role role : roles) {
            DetailedProjectRoleDTO detailedProjectRoleDTO = new DetailedProjectRoleDTO();
            BeanUtils.copyProperties(role, detailedProjectRoleDTO);
            if (role.getCreatorId() == 1) {
                detailedProjectRoleDTO.setCreator("系统");
            } else {
                detailedProjectRoleDTO.setCreator(creatorUsername);
            }
            List<Permission> permissions = permissionMapper.getByRoleId(role.getId());
            List<PermissionDTO> permissionDTOS = permissions.stream()
                    .map(o -> new PermissionDTO(o.getId(), o.getName(), o.getModule(), o.getRemark()))
                    .collect(Collectors.toList());
            detailedProjectRoleDTO.setPermissions(permissionDTOS);
            detailedProjectRoleDTOS.add(detailedProjectRoleDTO);
        }
        return new PageDTO<>(page.getCurrent(), page.getSize(), page.getTotal(), detailedProjectRoleDTOS);
    }

    public List<SimpleMemberDTO> getPrivilegedMembers(Integer projectId, List<Integer> privileges) {
        List<Integer> finalMemberIdList = null;
        for (Integer privilege : privileges) {
            List<Integer> memberIdList = userRoleMapper.selectUserIdByProjectIdAndPermissionId(projectId, privilege);
            if (finalMemberIdList == null) {
                finalMemberIdList = memberIdList;
            } else {
                finalMemberIdList.retainAll(memberIdList);
            }
        }
        return userService.getSimpleMemberList(finalMemberIdList);
    }
}
