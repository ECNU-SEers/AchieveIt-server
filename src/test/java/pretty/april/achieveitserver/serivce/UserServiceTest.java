package pretty.april.achieveitserver.serivce;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import pretty.april.achieveitserver.model.Username;
import pretty.april.achieveitserver.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
	
	@Autowired
	private UserService userService;
	
	@Test
	public void getAllSupervisorTest() {
		List<Username> allUsers = userService.getAllSupervisors();
		for (Username user: allUsers) {
			System.out.println(""+user.getId());
			System.out.println(""+user.getUsername());
		}
	}
}
