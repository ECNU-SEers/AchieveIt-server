package pretty.april.achieveitserver.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pretty.april.achieveitserver.dto.PageDTO;
import pretty.april.achieveitserver.dto.PermissionDTO;
import pretty.april.achieveitserver.dto.Response;
import pretty.april.achieveitserver.dto.RoleDTO;
import pretty.april.achieveitserver.entity.User;
import pretty.april.achieveitserver.request.AssignRoleRequest;
import pretty.april.achieveitserver.request.CreateRoleRequest;
import pretty.april.achieveitserver.request.EditRoleRequest;
import pretty.april.achieveitserver.request.RevokeRoleRequest;
import pretty.april.achieveitserver.service.AuthorityService;
import pretty.april.achieveitserver.service.UserService;
import pretty.april.achieveitserver.utils.ResponseUtils;

import javax.validation.Valid;
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

    @GetMapping("/permissions")
    public Response<List<PermissionDTO>> getPermissions(@RequestParam Integer pageSize,
                                                        @RequestParam Integer pageNo) {
        return ResponseUtils.successResponse(authorityService.getPermissions(pageNo, pageSize));
    }

    @GetMapping("/roles")
    public Response<PageDTO<RoleDTO>> getRoles(@RequestParam Integer pageSize,
                                               @RequestParam Integer page) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getByUsername(username);
        return ResponseUtils.successResponse(authorityService.getRoles(page, pageSize, user.getId()));
    }

    @PostMapping("/role")
    public Response<Integer> createRoles(@RequestBody @Valid CreateRoleRequest request) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getByUsername(username);
        return ResponseUtils.successResponse(authorityService.createRole(request, user.getId()));
    }

    @PutMapping("/role/{roleId}")
    public Response<?> editRole(@PathVariable Integer roleId, @RequestBody @Valid EditRoleRequest request) {
        authorityService.editRole(request, roleId);
        return ResponseUtils.successResponse();
    }

    @PostMapping("/role/{roleId}/assign")
    public Response<?> assignRole(@PathVariable Integer roleId, @RequestBody @Valid AssignRoleRequest request) {
        authorityService.assignRole(request, roleId);
        return ResponseUtils.successResponse();
    }

    @PutMapping("/role/{roleId}/revoke")
    public Response<?> revokeRole(@PathVariable Integer roleId, @RequestBody @Valid RevokeRoleRequest request) {
        authorityService.revokeRole(request, roleId);
        return ResponseUtils.successResponse();
    }
}
