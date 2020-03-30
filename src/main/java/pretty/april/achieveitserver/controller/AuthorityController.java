package pretty.april.achieveitserver.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pretty.april.achieveitserver.dto.PageDTO;
import pretty.april.achieveitserver.dto.PermissionDTO;
import pretty.april.achieveitserver.dto.Response;
import pretty.april.achieveitserver.dto.RoleDTO;
import pretty.april.achieveitserver.entity.User;
import pretty.april.achieveitserver.request.AssignRevokeRoleRequest;
import pretty.april.achieveitserver.request.CreateRoleRequest;
import pretty.april.achieveitserver.request.EditRoleRequest;
import pretty.april.achieveitserver.service.AuthorityService;
import pretty.april.achieveitserver.service.UserService;
import pretty.april.achieveitserver.utils.ResponseUtils;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
public class AuthorityController {

    private AuthorityService authorityService;

    private UserService userService;

    public AuthorityController(AuthorityService authorityService, UserService userService) {
        this.authorityService = authorityService;
        this.userService = userService;
    }

    /**
     * 获取所有的项目权限
     * @param pageSize
     * @param pageNo
     * @return
     */
    @GetMapping("/permissions")
    public Response<List<PermissionDTO>> getPermissions(@RequestParam Integer pageSize,
                                                        @RequestParam @Min(1) Integer pageNo) {
        return ResponseUtils.successResponse(authorityService.getPermissions(pageNo, pageSize));
    }

    /**
     * 获取当前用户可见的所有的项目角色
     * @param pageSize
     * @param page
     * @return
     */
    @GetMapping("/roles")
    public Response<PageDTO<RoleDTO>> getRoles(@RequestParam Integer pageSize,
                                               @RequestParam @Min(1) Integer page) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getByUsername(username);
        return ResponseUtils.successResponse(authorityService.getRoles(page, pageSize, user.getId()));
    }

    /**
     * 使用当前用户创建一个项目角色
     * @param request
     * @return
     */
    @PostMapping("/role")
    public Response<Integer> createRoles(@RequestBody @Valid CreateRoleRequest request) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getByUsername(username);
        return ResponseUtils.successResponse(authorityService.createRole(request, user.getId()));
    }

    /**
     * 编辑某个项目角色
     * @param roleId
     * @param request
     * @return
     */
    @PutMapping("/role/{roleId}")
    public Response<?> editRole(@PathVariable Integer roleId, @RequestBody @Valid EditRoleRequest request) {
        authorityService.editRole(request, roleId);
        return ResponseUtils.successResponse();
    }

    /**
     * 获取某个项目角色包含的所有权限
     * @param roleId
     * @return
     */
    @GetMapping("/role/{roleId}/permissions")
    public Response<List<PermissionDTO>> getRolePermissions(@PathVariable Integer roleId) {
        return ResponseUtils.successResponse(authorityService.getRolePermissions(roleId));
    }

    /**
     * 将某个角色分配给某个项目的某个成员
     * @param roleId
     * @param projectId
     * @param request
     * @return
     */
    @PostMapping("/project/{projectId}/role/{roleId}/assign")
    public Response<?> assignRole(@PathVariable Integer roleId, @PathVariable Integer projectId, @RequestBody @Valid AssignRevokeRoleRequest request) {
        authorityService.assignRole(request, roleId, projectId);
        return ResponseUtils.successResponse();
    }

    /**
     * 从某个项目的某个成员处撤回某个角色
     * @param roleId
     * @param projectId
     * @param request
     * @return
     */
    @PutMapping("/project/{projectId}/role/{roleId}/revoke")
    public Response<?> revokeRole(@PathVariable Integer roleId, @PathVariable Integer projectId, @RequestBody @Valid AssignRevokeRoleRequest request) {
        authorityService.revokeRole(request, roleId, projectId);
        return ResponseUtils.successResponse();
    }

    /**
     * 获取当前用户在某个项目内拥有的所有项目权限
     * @param projectId
     * @return
     */
    @GetMapping("/project/{projectId}/permissions/me")
    public Response<List<PermissionDTO>> getMyPermissions(@PathVariable Integer projectId) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getByUsername(username);
        return ResponseUtils.successResponse(authorityService.getMyPermissions(projectId, user.getId()));
    }

    /**
     * 获取当前用户在某个项目内拥有的所有项目角色
     * @param projectId
     * @return
     */
    @GetMapping("/project/{projectId}/roles/me")
    public Response<List<RoleDTO>> getMyRoles(@PathVariable Integer projectId) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getByUsername(username);
        return ResponseUtils.successResponse(authorityService.getMyRoles(projectId, user.getId()));
    }
}
