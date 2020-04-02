package pretty.april.achieveitserver.config;

//@Configuration
//@Slf4j
public class DatabaseInitConfig {

//    private final UserService userService;
//
//    private final RoleService roleService;
//
//    private final BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    public DatabaseInitConfig(UserService userService, RoleService roleService, BCryptPasswordEncoder bCryptPasswordEncoder) {
//        this.userService = userService;
//        this.roleService = roleService;
//        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//    }
//
//    @EventListener
//    @Transactional
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//        if (userService.getByUsername("admin") != null) {
//            log.warn("Admin initialization failed. Admin user already exists.");
//            return;
//        }
//        User user = new User();
//        user.setUsername("admin");
//        user.setPassword(bCryptPasswordEncoder.encode("admin"));
//        user.setDepartment("April");
//        user.setEmail("admin@april.com");
//        userService.insert(user);
//
//        Role role = roleService.getByRoleName("ROLE_ADMIN");
//        if (role == null) {
//            log.warn("Admin initialization failed. Admin role not exists.");
//            return;
//        }
//
//        UserRole userRole = new UserRole();
//        userRole.setUserId(user.getId());
//        userRole.setRoleId(role.getId());
//        userService.addUserRole(userRole);
//    }
}
