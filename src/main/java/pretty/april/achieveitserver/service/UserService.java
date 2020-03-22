package pretty.april.achieveitserver.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import pretty.april.achieveitserver.entity.User;
import pretty.april.achieveitserver.entity.UserRole;
import pretty.april.achieveitserver.mapper.UserMapper;
import pretty.april.achieveitserver.mapper.UserRoleMapper;
import pretty.april.achieveitserver.security.SecurityUser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private UserMapper userMapper;

    private UserRoleMapper userRoleMapper;

    public UserService(UserMapper userMapper, UserRoleMapper userRoleMapper) {
        this.userMapper = userMapper;
        this.userRoleMapper = userRoleMapper;
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
        List<String> authorities = userMapper.selectPermissionsByUserId(user.getId());
        Set<String> au = new HashSet<>(authorities);
        su.setAuthorities(au);
        return su;
    }

    public void insert(User user) {
        userMapper.insert(user);
    }

    public void addUserRole(UserRole userRole) {
        userRoleMapper.insert(userRole);
    }
}
