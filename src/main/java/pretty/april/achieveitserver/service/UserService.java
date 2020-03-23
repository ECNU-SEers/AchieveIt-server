package pretty.april.achieveitserver.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pretty.april.achieveitserver.dto.SimpleEmployeeDTO;
import pretty.april.achieveitserver.entity.User;
import pretty.april.achieveitserver.entity.UserSysRole;
import pretty.april.achieveitserver.mapper.UserMapper;
import pretty.april.achieveitserver.mapper.UserSysRoleMapper;
import pretty.april.achieveitserver.request.AddUserRequest;
import pretty.april.achieveitserver.security.SecurityUser;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserMapper userMapper;

    private UserSysRoleMapper userSysRoleMapper;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserMapper userMapper, UserSysRoleMapper userSysRoleMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userMapper = userMapper;
        this.userSysRoleMapper = userSysRoleMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User getByUsername(String username) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
    }

    public User getById(Integer id) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("id", id));
    }

    public SecurityUser getSecurityUserByUsername(String username) {
        User user = getByUsername(username);
        if (user == null) {
            return null;
        }
        SecurityUser su = new SecurityUser();
        su.setUsername(user.getUsername());
        su.setPassword(user.getPassword());
        List<String> authorities = userSysRoleMapper.selectRoleNamesByUserId(user.getId());
        Set<String> au = new HashSet<>(authorities);
        su.setAuthorities(au);
        return su;
    }

    public void addUser(AddUserRequest request) {
        if (null != userMapper.selectOne(new QueryWrapper<User>().eq("username", request.getUsername()))) {
            throw new IllegalArgumentException("Username already exists");
        }
        User user = new User();
        BeanUtils.copyProperties(request, user);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userMapper.insert(user);
        UserSysRole userSysRole = new UserSysRole();
        userSysRole.setUserId(user.getId());
        userSysRole.setSysRoleId(2);
        userSysRoleMapper.insert(userSysRole);
    }

    public List<SimpleEmployeeDTO> getEmployees() {
        List<User> users = userMapper.selectList(new QueryWrapper<User>().ne(true, "id", 1));
        return users.stream().map(u -> new SimpleEmployeeDTO(u.getId(), u.getUsername())).collect(Collectors.toList());
    }
}
