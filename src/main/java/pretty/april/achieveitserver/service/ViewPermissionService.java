package pretty.april.achieveitserver.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pretty.april.achieveitserver.dto.*;
import pretty.april.achieveitserver.entity.*;
import pretty.april.achieveitserver.mapper.*;
import pretty.april.achieveitserver.request.AddViewRoleRequest;
import pretty.april.achieveitserver.request.EditUserRolesRequest;
import pretty.april.achieveitserver.request.EditViewRoleRequest;
import pretty.april.achieveitserver.request.UserViewRoleRequest;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ViewPermissionService {

    private ViewRoleMapper viewRoleMapper;

    private ViewRolePermissionService viewRolePermissionService;

    private UserViewRoleService userViewRoleService;

    private ViewRolePermissionMapper viewRolePermissionMapper;

    private UserViewRoleMapper userViewRoleMapper;

    private ViewPermissionMapper viewPermissionMapper;

    private UserMapper userMapper;

    private ProjectMemberMapper projectMemberMapper;

    private UserService userService;

    public ViewPermissionService(ViewRoleMapper viewRoleMapper, ViewRolePermissionService viewRolePermissionService, UserViewRoleService userViewRoleService, ViewRolePermissionMapper viewRolePermissionMapper, UserViewRoleMapper userViewRoleMapper, ViewPermissionMapper viewPermissionMapper, UserMapper userMapper, ProjectMapper projectMapper, ProjectMemberMapper projectMemberMapper, UserService userService) {
        this.viewRoleMapper = viewRoleMapper;
        this.viewRolePermissionService = viewRolePermissionService;
        this.userViewRoleService = userViewRoleService;
        this.viewRolePermissionMapper = viewRolePermissionMapper;
        this.userViewRoleMapper = userViewRoleMapper;
        this.viewPermissionMapper = viewPermissionMapper;
        this.userMapper = userMapper;
        this.projectMemberMapper = projectMemberMapper;
        this.userService = userService;
    }

    public Map<String, Object> getViewPermissions(Integer userId) {
        List<UserViewRole> userViewRoles = userViewRoleMapper.selectList(new QueryWrapper<UserViewRole>()
                .eq("user_id", userId));
        List<Integer> roles = userViewRoles.stream().map(UserViewRole::getRoleId).collect(Collectors.toList());
        List<ViewPermission> viewPermissions;
        if (!CollectionUtils.isEmpty(roles)) {
            viewPermissions = viewRolePermissionMapper.selectViewPermissionWithModuleByRoleIdIn(roles);
        } else {
            viewPermissions = new ArrayList<>();
        }
        Map<String, Object> map = new HashMap<>();
        Map<String, List<ViewPermissionDTO>> permissions = viewPermissions.stream()
                .map(o -> new ViewPermissionDTO(o.getId(), o.getName(), o.getModule()))
                .collect(Collectors.groupingBy(ViewPermissionDTO::getModule));
        map.put("permissions", permissions);
        User user = userMapper.selectById(userId);
        SimpleUser simpleUser = new SimpleUser(user.getUsername(), user.getRealName());
        map.put("user", simpleUser);
        return map;
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
        if (newPermissions.equals(oldPermissions)) {
            return;
        }
        Set<Integer> common = new HashSet<>(newPermissions);
        common.retainAll(oldPermissions);
        newPermissions.removeAll(common);
        oldPermissions.removeAll(common);
        if (!CollectionUtils.isEmpty(oldPermissions)) {
            viewRolePermissionService.remove(new QueryWrapper<ViewRolePermission>().eq("role_id", roleId).in("permission_id", oldPermissions));
        }
        if (!CollectionUtils.isEmpty(newPermissions)) {
            List<ViewRolePermission> newViewRolePermissions = newPermissions.stream()
                    .map(o -> new ViewRolePermission(roleId, o)).collect(Collectors.toList());
            viewRolePermissionService.saveBatch(newViewRolePermissions);
        }
    }

    public List<ViewPermissionDTO> getAllPermissions() {
        List<ViewPermission> viewPermissions = viewPermissionMapper.selectList(new QueryWrapper<>());
        return viewPermissions.stream()
                .map(o -> new ViewPermissionDTO(o.getId(), o.getName(), o.getModule())).collect(Collectors.toList());
    }

    public List<DetailedViewRoleDTO> getDetailedRoles() {
        List<ViewRole> roles = viewRoleMapper.selectList(new QueryWrapper<>());
        List<DetailedViewRoleDTO> detailedViewRoleDTOS = new ArrayList<>();
        for (ViewRole viewRole : roles) {
            DetailedViewRoleDTO detailedViewRoleDTO = new DetailedViewRoleDTO();
            detailedViewRoleDTO.setId(viewRole.getId());
            detailedViewRoleDTO.setName(viewRole.getName());
            detailedViewRoleDTO.setCreator("系统");
            detailedViewRoleDTO.setRemark(viewRole.getRemark());
            List<ViewPermission> viewPermissions = viewPermissionMapper.selectByRoleId(viewRole.getId());
            List<ViewPermissionDTO> viewPermissionDTOS = viewPermissions.stream()
                    .map(o -> new ViewPermissionDTO(o.getId(), o.getName(), o.getModule())).collect(Collectors.toList());
            detailedViewRoleDTO.setPermissions(viewPermissionDTOS);
            detailedViewRoleDTOS.add(detailedViewRoleDTO);
        }
        return detailedViewRoleDTOS;
    }

    public void deleteViewRole(Integer roleId) {
        userViewRoleMapper.delete(new QueryWrapper<UserViewRole>().eq("role_id", roleId));
        viewRoleMapper.delete(new QueryWrapper<ViewRole>().eq("id", roleId));
    }

    public PageDTO<ViewRoleUserDTO> getViewRoleUsers(Integer pageNo, Integer pageSize, String keyword) {
        Page<User> userPage = new Page<>(pageNo, pageSize);
        IPage<User> users = userMapper.selectPage(userPage, new QueryWrapper<User>().like("real_name", keyword));
        List<User> userList = users.getRecords();
        List<ViewRoleUserDTO> viewRoleUserDTOS = new ArrayList<>();
        for (User user : userList) {
            ViewRoleUserDTO userDTO = new ViewRoleUserDTO();
            BeanUtils.copyProperties(user, userDTO);
            userDTO.setRoles(getUserViewRoles(user.getId()));
            userDTO.setProjects(projectMemberMapper.selectProjectNamesByUserIdAndState(user.getId(), "进行中"));
            viewRoleUserDTOS.add(userDTO);
        }
        return new PageDTO<>(users.getCurrent(), users.getSize(), users.getTotal(), viewRoleUserDTOS);
    }

    public List<ViewRoleDTO> getUserViewRoles(Integer userId) {
        List<ViewRole> viewRoles = userViewRoleMapper.getViewRolesByUserId(userId);
        return viewRoles.stream().map(o -> new ViewRoleDTO(o.getId(), o.getName(), o.getRemark())).collect(Collectors.toList());
    }

    public void editUserRoles(Integer userId, EditUserRolesRequest request) {
        userViewRoleMapper.delete(new QueryWrapper<UserViewRole>().eq("user_id", userId));
        if (CollectionUtils.isEmpty(request.getRoles())) {
            return;
        }
        int count = viewRoleMapper.selectCount(new QueryWrapper<ViewRole>().in("id", request.getRoles()));
        if (count != request.getRoles().size()) {
            throw new IllegalArgumentException("Cannot find roles");
        }
        List<UserViewRole> userViewRoles = request.getRoles().stream()
                .map(o -> new UserViewRole(userId, o)).collect(Collectors.toList());
        userViewRoleService.saveBatch(userViewRoles);
    }

    @Data
    @AllArgsConstructor
    static class SimpleUser {
        private String username;
        private String realName;
    }

    public List<SimpleMemberDTO> getPrivilegedMembers(Integer projectId, List<Integer> privileges) {
        List<Integer> finalMemberIdList = null;
        for (Integer privilege : privileges) {
            List<Integer> memberIdList = userViewRoleMapper.selectUserIdByProjectIdAndViewPermissionId(projectId, privilege);
            if (finalMemberIdList == null) {
                finalMemberIdList = memberIdList;
            } else {
                finalMemberIdList.retainAll(memberIdList);
            }
        }
        return userService.getSimpleMemberList(finalMemberIdList);
    }
}
