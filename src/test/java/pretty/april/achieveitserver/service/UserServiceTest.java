package pretty.april.achieveitserver.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import pretty.april.achieveitserver.entity.Employee;
import pretty.april.achieveitserver.model.Supervisor;
import pretty.april.achieveitserver.request.AddUserRequest;
import pretty.april.achieveitserver.request.AddUsersRequest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private EmployeeService employeeService;

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
    void addUsers() {
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee();
        employee.setJobNumber("test_employee1");
        employee.setDepartment("test");
        employee.setEmail("test");
        employee.setPhoneNumber("test");
        employee.setRealName("test");
        employee.setPosition("test");
        employees.add(employee);
        Employee anotherEmployee = new Employee();
        anotherEmployee.setJobNumber("test_employee2");
        anotherEmployee.setDepartment("test");
        anotherEmployee.setEmail("test");
        anotherEmployee.setPhoneNumber("test");
        anotherEmployee.setRealName("test");
        anotherEmployee.setPosition("test");
        employees.add(anotherEmployee);
        employeeService.saveBatch(employees);
        AddUsersRequest addUsersRequest = new AddUsersRequest();
        addUsersRequest.setUsernames(Arrays.asList("test_employee1", "test_employee2"));
        userService.addUsers(addUsersRequest);
    }

    @Test
    void getEmployees() {
        assertNotNull(userService.getEmployees());
    }

    @Test
    public void getByIdTest() {
        Integer id = 1;
        assertNotNull(userService.getById(id));
    }

    @Test
    public void getAllSupervisorTest() {
        List<Supervisor> allUsers = userService.getAllSupervisors();
        for (Supervisor user : allUsers) {
            System.out.println("id = " + user.getId());
            System.out.println("username = " + user.getUsername());
            System.out.println("realname = " + user.getRealName());
            System.out.println("============================");
        }
        assertNotNull(allUsers);
    }

    @Test
    public void getAllConfigOrganizerTest() {
        List<Supervisor> allUsers = userService.getAllConfigOrganizer();
        for (Supervisor user : allUsers) {
            System.out.println("id = " + user.getId());
            System.out.println("username = " + user.getUsername());
            System.out.println("realname = " + user.getRealName());
            System.out.println("============================");
        }
        assertNotNull(allUsers);
    }

    @Test
    public void getAllQAManagerTest() {
        List<Supervisor> allUsers = userService.getAllQAManager();
        for (Supervisor user : allUsers) {
            System.out.println("id = " + user.getId());
            System.out.println("username = " + user.getUsername());
            System.out.println("realname = " + user.getRealName());
            System.out.println("============================");
        }
        assertNotNull(allUsers);
    }

    @Test
    public void getAllEPGLeaderTest() {
        List<Supervisor> allUsers = userService.getAllEPGLeader();
        for (Supervisor user : allUsers) {
            System.out.println("id = " + user.getId());
            System.out.println("username = " + user.getUsername());
            System.out.println("realname = " + user.getRealName());
            System.out.println("============================");
        }
        assertNotNull(allUsers);
    }
}