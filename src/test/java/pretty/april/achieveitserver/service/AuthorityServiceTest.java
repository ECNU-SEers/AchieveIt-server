package pretty.april.achieveitserver.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import pretty.april.achieveitserver.request.AssignRevokeRoleRequest;
import pretty.april.achieveitserver.request.CreateRoleRequest;
import pretty.april.achieveitserver.request.EditRoleRequest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class AuthorityServiceTest {

    @Autowired
    private AuthorityService authorityService;

    @Test
    void getPermissions() {
        int page = 1;
        int pageSize = 5;
        assertNotNull(authorityService.getPermissions(page, pageSize));
    }

    @Test
    void getRoles() {
        int page = 1;
        int pageSize = 5;
        int creatorId = 1;
        assertNotNull(authorityService.getRoles(page, pageSize, creatorId));
    }

    @Test
    void createRole() {
        CreateRoleRequest request = new CreateRoleRequest();
        request.setName("test");
        request.setRemark("test");
        request.setPermissions(Collections.singletonList(2));
        int creatorId = 2;
        assertNotNull(authorityService.createRole(request, creatorId));
    }

    @Test
    void editRole() {
        CreateRoleRequest createRoleRequest = new CreateRoleRequest();
        createRoleRequest.setName("test1");
        createRoleRequest.setRemark("test1");
        createRoleRequest.setPermissions(Collections.singletonList(2));
        int creatorId = 2;
        Integer roleId = authorityService.createRole(createRoleRequest, creatorId);
        assertNotNull(roleId);
        EditRoleRequest editRoleRequest = new EditRoleRequest();
        editRoleRequest.setName("test2");
        editRoleRequest.setRemark("test2");
        editRoleRequest.setPermissions(Collections.singletonList(3));
        authorityService.editRole(editRoleRequest, roleId);
    }

    @Test
    void assignRole() {
        CreateRoleRequest createRoleRequest = new CreateRoleRequest();
        createRoleRequest.setName("test");
        createRoleRequest.setRemark("test");
        createRoleRequest.setPermissions(Collections.singletonList(2));
        int creatorId = 1;
        Integer roleId = authorityService.createRole(createRoleRequest, creatorId);
        assertNotNull(roleId);
        AssignRevokeRoleRequest request = new AssignRevokeRoleRequest();
        request.setAssigneeId(1);
        int projectId = 1;
        authorityService.assignRole(request, roleId, projectId);
    }

    @Test
    void revokeRole() {
        CreateRoleRequest createRoleRequest = new CreateRoleRequest();
        createRoleRequest.setName("test");
        createRoleRequest.setRemark("test");
        createRoleRequest.setPermissions(Collections.singletonList(2));
        int creatorId = 1;
        Integer roleId = authorityService.createRole(createRoleRequest, creatorId);
        assertNotNull(roleId);
        AssignRevokeRoleRequest request = new AssignRevokeRoleRequest();
        request.setAssigneeId(1);
        int projectId = 1;
        authorityService.assignRole(request, roleId, projectId);
        authorityService.revokeRole(request, roleId, projectId);
    }

    @Test
    void getMyPermissions() {
        int projectId = 1;
        int userId = 1;
        assertNotNull(authorityService.getMyPermissions(projectId, userId));
    }

    @Test
    void getMyRoles() {
        int projectId = 1;
        int userId = 1;
        assertNotNull(authorityService.getMyRoles(projectId, userId));
    }

    @Test
    void getRolePermissions() {
        CreateRoleRequest createRoleRequest = new CreateRoleRequest();
        createRoleRequest.setName("test");
        createRoleRequest.setRemark("test");
        createRoleRequest.setPermissions(Collections.singletonList(2));
        int creatorId = 1;
        Integer roleId = authorityService.createRole(createRoleRequest, creatorId);
        assertNotNull(roleId);
        assertNotNull(authorityService.getRolePermissions(roleId));
    }
}