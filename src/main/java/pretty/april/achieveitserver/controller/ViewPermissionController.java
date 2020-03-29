package pretty.april.achieveitserver.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pretty.april.achieveitserver.dto.DetailedViewRoleDTO;
import pretty.april.achieveitserver.dto.Response;
import pretty.april.achieveitserver.dto.ViewPermissionDTO;
import pretty.april.achieveitserver.dto.ViewRoleDTO;
import pretty.april.achieveitserver.entity.User;
import pretty.april.achieveitserver.request.AddViewRoleRequest;
import pretty.april.achieveitserver.request.EditViewRoleRequest;
import pretty.april.achieveitserver.request.UserViewRoleRequest;
import pretty.april.achieveitserver.service.UserService;
import pretty.april.achieveitserver.service.ViewPermissionService;
import pretty.april.achieveitserver.utils.ResponseUtils;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ViewPermissionController {

    private ViewPermissionService viewPermissionService;

    private UserService userService;

    public ViewPermissionController(ViewPermissionService viewPermissionService, UserService userService) {
        this.viewPermissionService = viewPermissionService;
        this.userService = userService;
    }

    @GetMapping("/view/permissions/me")
    public Response<Map<String, List<ViewPermissionDTO>>> getViewPermissions() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getByUsername(username);
        return ResponseUtils.successResponse(viewPermissionService.getViewPermissions(user.getId()));
    }

    @PostMapping("/view/role")
    public Response<Integer> addRole(@RequestBody @Valid AddViewRoleRequest request) {
        return ResponseUtils.successResponse(viewPermissionService.addRole(request));
    }

    @GetMapping("/view/roles")
    public Response<List<DetailedViewRoleDTO>> getRoles() {
        return ResponseUtils.successResponse(viewPermissionService.getDetailedRoles());
    }

    @PostMapping("/view/role/{roleId}/assign")
    public Response<?> assignRole(@PathVariable Integer roleId, @RequestBody @Valid UserViewRoleRequest request) {
        viewPermissionService.assignRole(roleId, request);
        return ResponseUtils.successResponse();
    }

    @PostMapping("/view/role/{roleId}/revoke")
    public Response<?> revokeRole(@PathVariable Integer roleId, @RequestBody @Valid UserViewRoleRequest request) {
        viewPermissionService.revokeRole(roleId, request);
        return ResponseUtils.successResponse();
    }

    @PutMapping("/view/role/{roleId}")
    public Response<?> editRole(@PathVariable Integer roleId, @RequestBody @Valid EditViewRoleRequest request) {
        viewPermissionService.editRole(roleId, request);
        return ResponseUtils.successResponse();
    }

    @GetMapping("/view/permissions")
    public Response<List<ViewPermissionDTO>> getPermissions() {
        return ResponseUtils.successResponse(viewPermissionService.getAllPermissions());
    }
}
