package pretty.april.achieveitserver.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RolePermissionServiceTest {

	@Autowired
	private RolePermissionService rolePermissionService;
	
	@Test
	public void checkRolePermissionExistByRoleIdAndPermissionIdTest() {
		Integer roleId = new Integer(4);
		Integer permissionId = new Integer(2);
		assertTrue(rolePermissionService.checkRolePermissionExistByRoleIdAndPermissionId(roleId, permissionId));
	}
}
