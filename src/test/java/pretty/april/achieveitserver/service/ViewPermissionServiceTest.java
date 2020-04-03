package pretty.april.achieveitserver.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import pretty.april.achieveitserver.request.AddViewRoleRequest;
import pretty.april.achieveitserver.request.EditUserRolesRequest;
import pretty.april.achieveitserver.request.EditViewRoleRequest;
import pretty.april.achieveitserver.request.UserViewRoleRequest;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class ViewPermissionServiceTest {

    @Autowired
    private ViewPermissionService viewPermissionService;

    @Test
    void getViewPermissions() {
        int userId = 1;
        assertNotNull(viewPermissionService.getViewPermissions(userId));
    }

    @Test
    void addRole() {
        AddViewRoleRequest addViewRoleRequest = new AddViewRoleRequest();
        addViewRoleRequest.setName("test");
        addViewRoleRequest.setRemark("test");
        addViewRoleRequest.setPermissions(Arrays.asList(5, 6));
        Integer roleId = viewPermissionService.addRole(addViewRoleRequest);
        assertNotNull(roleId);
    }

    @Test
    void getRoles() {
        AddViewRoleRequest addViewRoleRequest = new AddViewRoleRequest();
        addViewRoleRequest.setName("test");
        addViewRoleRequest.setRemark("test");
        addViewRoleRequest.setPermissions(Arrays.asList(5, 6));
        Integer roleId = viewPermissionService.addRole(addViewRoleRequest);
        assertNotNull(roleId);
        assertNotNull(viewPermissionService.getRoles());
    }

    @Test
    void assignRole() {
        AddViewRoleRequest addViewRoleRequest = new AddViewRoleRequest();
        addViewRoleRequest.setName("test");
        addViewRoleRequest.setRemark("test");
        addViewRoleRequest.setPermissions(Arrays.asList(5, 6));
        Integer roleId = viewPermissionService.addRole(addViewRoleRequest);
        assertNotNull(roleId);
        UserViewRoleRequest userViewRoleRequest = new UserViewRoleRequest();
        userViewRoleRequest.setUserId(1);
        viewPermissionService.assignRole(roleId, userViewRoleRequest);
    }

    @Test
    void revokeRole() {
        AddViewRoleRequest addViewRoleRequest = new AddViewRoleRequest();
        addViewRoleRequest.setName("test");
        addViewRoleRequest.setRemark("test");
        addViewRoleRequest.setPermissions(Arrays.asList(5, 6));
        Integer roleId = viewPermissionService.addRole(addViewRoleRequest);
        assertNotNull(roleId);
        UserViewRoleRequest userViewRoleRequest = new UserViewRoleRequest();
        userViewRoleRequest.setUserId(1);
        viewPermissionService.revokeRole(roleId, userViewRoleRequest);
    }

    @Test
    void editRole() {
        AddViewRoleRequest addViewRoleRequest = new AddViewRoleRequest();
        addViewRoleRequest.setName("test");
        addViewRoleRequest.setRemark("test");
        addViewRoleRequest.setPermissions(Arrays.asList(5, 6));
        Integer roleId = viewPermissionService.addRole(addViewRoleRequest);
        assertNotNull(roleId);
        EditViewRoleRequest editViewRoleRequest = new EditViewRoleRequest();
        editViewRoleRequest.setPermissions(Arrays.asList(5, 7, 8));
        editViewRoleRequest.setName("test2");
        viewPermissionService.editRole(roleId, editViewRoleRequest);
    }

    @Test
    void getAllPermissions() {
        assertNotNull(viewPermissionService.getAllPermissions());
    }

    @Test
    void getDetailedRoles() {
        AddViewRoleRequest addViewRoleRequest = new AddViewRoleRequest();
        addViewRoleRequest.setName("test");
        addViewRoleRequest.setRemark("test");
        addViewRoleRequest.setPermissions(Arrays.asList(5, 6));
        Integer roleId = viewPermissionService.addRole(addViewRoleRequest);
        assertNotNull(roleId);
        assertNotNull(viewPermissionService.getDetailedRoles());
    }

    @Test
    void deleteViewRole() {
        AddViewRoleRequest addViewRoleRequest = new AddViewRoleRequest();
        addViewRoleRequest.setName("test");
        addViewRoleRequest.setRemark("test");
        addViewRoleRequest.setPermissions(Arrays.asList(5, 6));
        Integer roleId = viewPermissionService.addRole(addViewRoleRequest);
        assertNotNull(roleId);
        viewPermissionService.deleteViewRole(roleId);
    }

    @Test
    void getViewRoleUsers() {
        String keyword = "";
        assertNotNull(viewPermissionService.getViewRoleUsers(1, 5, keyword));
    }

    @Test
    void getUserViewRoles() {
        viewPermissionService.getUserViewRoles(1);
    }

    @Test
    void editUserRoles() {
        AddViewRoleRequest addViewRoleRequest = new AddViewRoleRequest();
        addViewRoleRequest.setName("test");
        addViewRoleRequest.setRemark("test");
        addViewRoleRequest.setPermissions(Arrays.asList(5, 6));
        Integer roleId = viewPermissionService.addRole(addViewRoleRequest);
        assertNotNull(roleId);
        EditUserRolesRequest editUserRolesRequest = new EditUserRolesRequest();
        editUserRolesRequest.setRoles(Collections.singletonList(roleId));
        viewPermissionService.editUserRoles(1, editUserRolesRequest);
    }
}