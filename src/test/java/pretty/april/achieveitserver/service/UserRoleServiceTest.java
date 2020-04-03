package pretty.april.achieveitserver.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class UserRoleServiceTest {

	@Autowired
    private UserRoleService userRoleService;
	
	@Test
	public void checkUserRoleExistByUserIdAndRoleIdTest_true() {
		Integer userId = new Integer(1);
		Integer roleId = new Integer(2);
		assertTrue(userRoleService.checkUserRoleExistByUserIdAndRoleId(userId, roleId));
	}
	
	@Test
	public void checkUserRoleExistByUserIdAndRoleIdTest_false() {
		Integer userId = new Integer(1);
		Integer roleId = new Integer(1);
		assertFalse(userRoleService.checkUserRoleExistByUserIdAndRoleId(userId, roleId));
	}
	
	@Test
	public void getUserRoleByUserIdTest() {
		Integer userId = new Integer(1);
		assertNotNull(userRoleService.getUserRoleByUserId(userId));
	}
}
