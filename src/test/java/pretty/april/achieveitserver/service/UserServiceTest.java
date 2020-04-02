package pretty.april.achieveitserver.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import pretty.april.achieveitserver.model.Supervisor;
import pretty.april.achieveitserver.request.AddUserRequest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void getByUsername() {
        String username = "0987test";
        AddUserRequest addUserRequest = new AddUserRequest();
        addUserRequest.setDepartment("test");
        addUserRequest.setEmail("test");
        addUserRequest.setPassword("test");
        addUserRequest.setPhoneNumber("11");
        addUserRequest.setRealName("test");
        addUserRequest.setUsername(username);
        userService.addUser(addUserRequest);
        assertNotNull(userService.getByUsername(username));
    }

    @Test
    void addUser() {
        AddUserRequest addUserRequest = new AddUserRequest();
        addUserRequest.setDepartment("test");
        addUserRequest.setEmail("test");
        addUserRequest.setPassword("test");
        addUserRequest.setPhoneNumber("11");
        addUserRequest.setRealName("test");
        addUserRequest.setUsername("0987test");
        userService.addUser(addUserRequest);
    }

    @Test
    void getEmployees() {
        assertNotNull(userService.getEmployees());
    }
    
    @Test
	public void getAllSupervisorTest() {
		List<Supervisor> allUsers = userService.getAllSupervisors();
		for (Supervisor user: allUsers) {
			System.out.println("id = "+user.getId());
			System.out.println("username = "+user.getUsername());
			System.out.println("realname = "+user.getRealName());
			System.out.println("============================");
		}
		assertNotNull(allUsers);
	}
	
	@Test
	public void getAllConfigOrganizerTest() {
		List<Supervisor> allUsers = userService.getAllConfigOrganizer();
		for (Supervisor user: allUsers) {
			System.out.println("id = "+user.getId());
			System.out.println("username = "+user.getUsername());
			System.out.println("realname = "+user.getRealName());
			System.out.println("============================");
		}
		assertNotNull(allUsers);
	}
	
	@Test
	public void getAllQAManagerTest() {
		List<Supervisor> allUsers = userService.getAllQAManager();
		for (Supervisor user: allUsers) {
			System.out.println("id = "+user.getId());
			System.out.println("username = "+user.getUsername());
			System.out.println("realname = "+user.getRealName());
			System.out.println("============================");
		}
		assertNotNull(allUsers);
	}
	
	@Test
	public void getAllEPGLeaderTest() {
		List<Supervisor> allUsers = userService.getAllEPGLeader();
		for (Supervisor user: allUsers) {
			System.out.println("id = "+user.getId());
			System.out.println("username = "+user.getUsername());
			System.out.println("realname = "+user.getRealName());
			System.out.println("============================");
		}
		assertNotNull(allUsers);
	}
}