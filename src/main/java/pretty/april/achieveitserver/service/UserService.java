package pretty.april.achieveitserver.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import pretty.april.achieveitserver.entity.User;
import pretty.april.achieveitserver.entity.UserRole;
import pretty.april.achieveitserver.mapper.UserMapper;
import pretty.april.achieveitserver.mapper.UserRoleMapper;
import pretty.april.achieveitserver.mapper.UserSysRoleMapper;
import pretty.april.achieveitserver.security.SecurityUser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private UserMapper userMapper;

    private UserRoleMapper userRoleMapper;

    private UserSysRoleMapper userSysRoleMapper;

    public UserService(UserMapper userMapper, UserRoleMapper userRoleMapper, UserSysRoleMapper userSysRoleMapper) {
        this.userMapper = userMapper;
        this.userRoleMapper = userRoleMapper;
        this.userSysRoleMapper = userSysRoleMapper;
    }

    public User getByUsername(String username) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
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

    public void insert(User user) {
        userMapper.insert(user);
    }

    public void addUserRole(UserRole userRole) {
        userRoleMapper.insert(userRole);
    }
}
