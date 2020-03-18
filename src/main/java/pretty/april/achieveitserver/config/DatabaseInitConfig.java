package pretty.april.achieveitserver.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import pretty.april.achieveitserver.entity.Role;
import pretty.april.achieveitserver.entity.User;
import pretty.april.achieveitserver.entity.UserRole;
import pretty.april.achieveitserver.service.RoleService;
import pretty.april.achieveitserver.service.UserService;

@Configuration
@Slf4j
public class DatabaseInitConfig {

    private final UserService userService;

    private final RoleService roleService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public DatabaseInitConfig(UserService userService, RoleService roleService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @EventListener
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (userService.getByUsername("admin") != null) {
            log.warn("Database initialization failed. Admin user already exists.");
            return;
        }
        User user = new User();
        user.setUsername("admin");
        user.setPassword(bCryptPasswordEncoder.encode("admin"));
        user.setDepartment("April");
        user.setEmail("admin@april.com");
        userService.insert(user);

        if (roleService.getByRoleName("ROLE_ADMIN") != null) {
            return;
        }
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        roleService.insert(role);

        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(role.getId());
        userService.addUserRole(userRole);
    }
}
