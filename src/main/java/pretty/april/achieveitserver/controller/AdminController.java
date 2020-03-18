package pretty.april.achieveitserver.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pretty.april.achieveitserver.request.AddUserRequest;
import pretty.april.achieveitserver.dto.Response;
import pretty.april.achieveitserver.mapper.UserMapper;
import pretty.april.achieveitserver.entity.User;
import pretty.april.achieveitserver.utils.ResponseUtils;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserMapper userMapper;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AdminController(UserMapper userMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/addUser")
    public Response<?> addUser(@RequestBody AddUserRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", request.getUsername());
        if (!CollectionUtils.isEmpty(userMapper.selectByMap(map))) {
            return new Response<>();
        }
        User user = new User();
        BeanUtils.copyProperties(request, user);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userMapper.insert(user);
        return ResponseUtils.successResponse();
    }
}
