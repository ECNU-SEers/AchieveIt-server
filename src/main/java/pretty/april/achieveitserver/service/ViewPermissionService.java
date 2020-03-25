package pretty.april.achieveitserver.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import pretty.april.achieveitserver.dto.ViewPermissionDTO;
import pretty.april.achieveitserver.dto.ViewRoleDTO;
import pretty.april.achieveitserver.entity.UserViewRole;
import pretty.april.achieveitserver.entity.ViewRole;
import pretty.april.achieveitserver.entity.ViewRolePermission;
import pretty.april.achieveitserver.mapper.UserViewRoleMapper;
import pretty.april.achieveitserver.mapper.ViewPermissionMapper;
import pretty.april.achieveitserver.mapper.ViewRoleMapper;
import pretty.april.achieveitserver.mapper.ViewRolePermissionMapper;
import pretty.april.achieveitserver.model.ViewModulePermission;
import pretty.april.achieveitserver.request.AddViewRoleRequest;
import pretty.april.achieveitserver.request.EditViewRoleRequest;
import pretty.april.achieveitserver.request.UserViewRoleRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ViewPermissionService {

    private ViewRoleMapper viewRoleMapper;

    private ViewRolePermissionService viewRolePermissionService;

    private ViewRolePermissionMapper viewRolePermissionMapper;

    private UserViewRoleMapper userViewRoleMapper;

    private ViewPermissionMapper viewPermissionMapper;

    public ViewPermissionService(ViewRoleMapper viewRoleMapper, ViewRolePermissionService viewRolePermissionService, ViewRolePermissionMapper viewRolePermissionMapper, UserViewRoleMapper userViewRoleMapper, ViewPermissionMapper viewPermissionMapper) {
        this.viewRoleMapper = viewRoleMapper;
        this.viewRolePermissionService = viewRolePermissionService;
        this.viewRolePermissionMapper = viewRolePermissionMapper;
        this.userViewRoleMapper = userViewRoleMapper;
        this.viewPermissionMapper = viewPermissionMapper;
    }

    public Map<String, List<ViewPermissionDTO>> getViewPermissions(Integer userId) {
        List<UserViewRole> userViewRoles = userViewRoleMapper.selectList(new QueryWrapper<UserViewRole>()
                .eq("user_id", userId));
        List<Integer> roles = userViewRoles.stream().map(UserViewRole::getRoleId).collect(Collectors.toList());
        List<ViewModulePermission> viewModulePermissions = viewRolePermissionMapper.selectViewPermissionWithModuleByRoleIdIn(roles);
        return viewModulePermissions.stream()
                .map(o -> new ViewPermissionDTO(o.getPermissionId(), o.getName(), o.getModule()))
                .collect(Collectors.groupingBy(ViewPermissionDTO::getModule));
    }

    public Integer addRole(AddViewRoleRequest request) {
        ViewRole role = new ViewRole();
        role.setName(request.getName());
        role.setRemark(request.getRemark());
        viewRoleMapper.insert(role);
        List<ViewRolePermission> viewRolePermissions = request.getPermissions().stream()
                .map(o -> new ViewRolePermission(role.getId(), o)).collect(Collectors.toList());
        viewRolePermissionService.saveBatch(viewRolePermissions);
        return role.getId();
    }

    public List<ViewRoleDTO> getRoles() {
        List<ViewRole> roles = viewRoleMapper.selectList(new QueryWrapper<>());
        return roles.stream().map(o -> new ViewRoleDTO(o.getId(), o.getName(), o.getRemark())).collect(Collectors.toList());
    }

    public void assignRole(Integer roleId, UserViewRoleRequest request) {
        UserViewRole userViewRole = new UserViewRole();
        userViewRole.setRoleId(roleId);
        userViewRole.setUserId(request.getUserId());
        userViewRoleMapper.insert(userViewRole);
    }

    public void revokeRole(Integer roleId, UserViewRoleRequest request) {
        userViewRoleMapper.delete(new QueryWrapper<UserViewRole>()
                .eq("role_id", roleId).eq("user_id", request.getUserId()));
    }

    public void editRole(Integer roleId, EditViewRoleRequest request) {
        ViewRole role = new ViewRole();
        role.setRemark(request.getRemark());
        role.setName(request.getName());
        role.setId(roleId);
        viewRoleMapper.updateById(role);
        Set<Integer> newPermissions = new HashSet<>(request.getPermissions());
        List<ViewRolePermission> viewRolePermissions = viewRolePermissionService.list(
                new QueryWrapper<ViewRolePermission>().eq("role_id", roleId));
        Set<Integer> oldPermissions = viewRolePermissions.stream().map(ViewRolePermission::getPermissionId).collect(Collectors.toSet());
        Set<Integer> common = new HashSet<>(newPermissions);
        common.retainAll(oldPermissions);
        newPermissions.removeAll(common);
        oldPermissions.removeAll(common);
        viewRolePermissionService.remove(new QueryWrapper<ViewRolePermission>()
                .eq("role_id", roleId)
                .in("permission_id", oldPermissions));
        List<ViewRolePermission> newViewRolePermissions = newPermissions.stream()
                .map(o -> new ViewRolePermission(roleId, o)).collect(Collectors.toList());
        viewRolePermissionService.saveBatch(newViewRolePermissions);
    }

    public List<ViewPermissionDTO> getAllPermissions() {
        List<ViewModulePermission> viewModulePermissions = viewPermissionMapper.selectAllViewPermissionsWithModule();
        return viewModulePermissions.stream()
                .map(o -> new ViewPermissionDTO(o.getPermissionId(), o.getName(), o.getModule())).collect(Collectors.toList());
    }
}

@Service
class ViewRolePermissionService extends ServiceImpl<ViewRolePermissionMapper, ViewRolePermission> {
}
