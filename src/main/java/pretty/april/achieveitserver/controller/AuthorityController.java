package pretty.april.achieveitserver.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pretty.april.achieveitserver.dto.*;
import pretty.april.achieveitserver.request.AssignRevokeRoleRequest;
import pretty.april.achieveitserver.request.CreateRoleRequest;
import pretty.april.achieveitserver.request.EditRoleRequest;
import pretty.april.achieveitserver.security.UserContext;
import pretty.april.achieveitserver.service.AuthorityService;
import pretty.april.achieveitserver.utils.ResponseUtils;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
public class AuthorityController {

    private AuthorityService authorityService;

    public AuthorityController(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    /**
     * 获取所有的项目权限
     *
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
     *
     * @param pageSize
     * @param page
     * @return
     */
    public Response<PageDTO<RoleDTO>> getRoles(@RequestParam Integer pageSize,
                                               @RequestParam @Min(1) Integer page) {
        UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseUtils.successResponse(authorityService.getRoles(page, pageSize, userContext.getUserId()));
    }

    /**
     * 获取当前用户可见的所有项目角色和其包含的项目权限信息
     *
     * @param pageSize
     * @param page
     * @return
     */
    @GetMapping("/roles")
    public Response<PageDTO<DetailedProjectRoleDTO>> getDetailedRoles(@RequestParam Integer pageSize,
                                                                      @RequestParam @Min(1) Integer page) {
        UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseUtils.successResponse(authorityService.getDetailedRoles(page, pageSize, userContext.getUserId(), userContext.getUsername()));
    }

    /**
     * 使用当前用户创建一个项目角色
     *
     * @param request
     * @return
     */
    @PostMapping("/role")
    public Response<Integer> createRoles(@RequestBody @Valid CreateRoleRequest request) {
        UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseUtils.successResponse(authorityService.createRole(request, userContext.getUserId()));
    }

    /**
     * 编辑某个项目角色
     *
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
     *
     * @param roleId
     * @return
     */
    @GetMapping("/role/{roleId}/permissions")
    public Response<List<PermissionDTO>> getRolePermissions(@PathVariable Integer roleId) {
        return ResponseUtils.successResponse(authorityService.getRolePermissions(roleId));
    }

    /**
     * 将某个角色分配给某个项目的某个成员
     *
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
     *
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
     *
     * @param projectId
     * @return
     */
    @GetMapping("/project/{projectId}/permissions/me")
    public Response<List<PermissionDTO>> getMyPermissions(@PathVariable Integer projectId) {
        UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseUtils.successResponse(authorityService.getMyPermissions(projectId, userContext.getUserId()));
    }

    /**
     * 获取当前用户在某个项目内拥有的所有项目角色
     *
     * @param projectId
     * @return
     */
    @GetMapping("/project/{projectId}/roles/me")
    public Response<List<RoleDTO>> getMyRoles(@PathVariable Integer projectId) {
        UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseUtils.successResponse(authorityService.getMyRoles(projectId, userContext.getUserId()));
    }
}
