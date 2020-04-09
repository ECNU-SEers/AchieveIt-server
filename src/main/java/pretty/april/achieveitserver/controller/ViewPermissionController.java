package pretty.april.achieveitserver.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pretty.april.achieveitserver.dto.*;
import pretty.april.achieveitserver.request.AddViewRoleRequest;
import pretty.april.achieveitserver.request.EditUserRolesRequest;
import pretty.april.achieveitserver.request.EditViewRoleRequest;
import pretty.april.achieveitserver.request.UserViewRoleRequest;
import pretty.april.achieveitserver.security.UserContext;
import pretty.april.achieveitserver.service.ViewPermissionService;
import pretty.april.achieveitserver.utils.ResponseUtils;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ViewPermissionController {

    private ViewPermissionService viewPermissionService;

    public ViewPermissionController(ViewPermissionService viewPermissionService) {
        this.viewPermissionService = viewPermissionService;
    }

    /**
     * 获取当前用户的所有平台权限和用户基本信息
     *
     * @return
     */
    @GetMapping("/view/permissions/me")
    public Response<Map<String, Object>> getViewPermissions() {
        UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseUtils.successResponse(viewPermissionService.getViewPermissions(userContext.getUserId()));
    }

    /**
     * 添加一个平台权限角色
     *
     * @param request
     * @return
     */
    @PostMapping("/view/role")
    public Response<Integer> addRole(@RequestBody @Valid AddViewRoleRequest request) {
        return ResponseUtils.successResponse(viewPermissionService.addRole(request));
    }

    /**
     * 获取所有平台权限角色
     *
     * @return
     */
    @GetMapping("/view/roles")
    public Response<List<DetailedViewRoleDTO>> getRoles() {
        return ResponseUtils.successResponse(viewPermissionService.getDetailedRoles());
    }

    /**
     * 将某个角色分配给某个用户
     *
     * @param roleId
     * @param request
     * @return
     */
    @PostMapping("/view/role/{roleId}/assign")
    public Response<?> assignRole(@PathVariable Integer roleId, @RequestBody @Valid UserViewRoleRequest request) {
        viewPermissionService.assignRole(roleId, request);
        return ResponseUtils.successResponse();
    }

    /**
     * 从某个用户处收回已分配的某个角色
     *
     * @param roleId
     * @param request
     * @return
     */
    @PostMapping("/view/role/{roleId}/revoke")
    public Response<?> revokeRole(@PathVariable Integer roleId, @RequestBody @Valid UserViewRoleRequest request) {
        viewPermissionService.revokeRole(roleId, request);
        return ResponseUtils.successResponse();
    }

    /**
     * 编辑某个角色
     *
     * @param roleId
     * @param request
     * @return
     */
    @PutMapping("/view/role/{roleId}")
    public Response<?> editRole(@PathVariable Integer roleId, @RequestBody @Valid EditViewRoleRequest request) {
        viewPermissionService.editRole(roleId, request);
        return ResponseUtils.successResponse();
    }

    /**
     * 获取所有平台权限
     *
     * @return
     */
    @GetMapping("/view/permissions")
    public Response<List<ViewPermissionDTO>> getPermissions() {
        return ResponseUtils.successResponse(viewPermissionService.getAllPermissions());
    }

    /**
     * 删除某个角色
     *
     * @param roleId
     * @return
     */
    @DeleteMapping("/view/role/{roleId}")
    public Response<?> deleteViewRole(@PathVariable Integer roleId) {
        viewPermissionService.deleteViewRole(roleId);
        return ResponseUtils.successResponse();
    }

    /**
     * 分页获取所有用户的角色信息
     *
     * @param pageSize
     * @param page
     * @param keyword
     * @return
     */
    @GetMapping("/users/view/roles")
    public Response<PageDTO<ViewRoleUserDTO>> getViewPermissionUsers(@RequestParam Integer pageSize,
                                                                     @RequestParam @Min(1) Integer page,
                                                                     @RequestParam(required = false, defaultValue = "") String keyword) {
        return ResponseUtils.successResponse(viewPermissionService.getViewRoleUsers(page, pageSize, keyword));
    }

    /**
     * 编辑某个用户拥有的角色
     *
     * @param userId
     * @param request
     * @return
     */
    @PutMapping("/user/{userId}/view/roles")
    public Response<?> editUserRoles(@PathVariable Integer userId, @RequestBody EditUserRolesRequest request) {
        viewPermissionService.editUserRoles(userId, request);
        return ResponseUtils.successResponse();
    }
}
