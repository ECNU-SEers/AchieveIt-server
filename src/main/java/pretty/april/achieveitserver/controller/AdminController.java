package pretty.april.achieveitserver.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pretty.april.achieveitserver.entity.UserSysRole;
import pretty.april.achieveitserver.enums.ErrorCode;
import pretty.april.achieveitserver.mapper.UserSysRoleMapper;
import pretty.april.achieveitserver.request.AddUserRequest;
import pretty.april.achieveitserver.dto.Response;
import pretty.april.achieveitserver.mapper.UserMapper;
import pretty.april.achieveitserver.entity.User;
import pretty.april.achieveitserver.utils.ResponseUtils;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private final UserMapper userMapper;

    private UserSysRoleMapper userSysRoleMapper;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AdminController(UserMapper userMapper, UserSysRoleMapper userSysRoleMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userMapper = userMapper;
        this.userSysRoleMapper = userSysRoleMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/addUser")
    public Response<?> addUser(@RequestBody AddUserRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", request.getUsername());
        if (!CollectionUtils.isEmpty(userMapper.selectByMap(map))) {
            return ResponseUtils.errorResponse(ErrorCode.INVALID_ARGUMENT);
        }
        User user = new User();
        BeanUtils.copyProperties(request, user);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userMapper.insert(user);
        UserSysRole userSysRole = new UserSysRole();
        userSysRole.setUserId(user.getId());
        userSysRole.setSysRoleId(2);
        userSysRoleMapper.insert(userSysRole);
        return ResponseUtils.successResponse();
    }
}
