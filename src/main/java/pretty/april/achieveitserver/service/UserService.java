package pretty.april.achieveitserver.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pretty.april.achieveitserver.dto.SimpleEmployeeDTO;
import pretty.april.achieveitserver.entity.User;
import pretty.april.achieveitserver.mapper.UserMapper;
import pretty.april.achieveitserver.mapper.ViewRolePermissionMapper;
import pretty.april.achieveitserver.model.Supervisor;
import pretty.april.achieveitserver.request.AddUserRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserMapper userMapper;
    private ViewRolePermissionMapper viewRolePermissionMapper;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserMapper userMapper, ViewRolePermissionMapper viewRolePermissionMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userMapper = userMapper;
        this.viewRolePermissionMapper = viewRolePermissionMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User getByUsername(String username) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
    }

    public User getById(Integer id) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("id", id));
    }

    public void addUser(AddUserRequest request) {
        if (null != userMapper.selectOne(new QueryWrapper<User>().eq("username", request.getUsername()))) {
            throw new IllegalArgumentException("Username already exists");
        }
        User user = new User();
        BeanUtils.copyProperties(request, user);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userMapper.insert(user);
    }

    public List<SimpleEmployeeDTO> getEmployees() {
        List<User> users = userMapper.selectList(new QueryWrapper<User>().ne(true, "id", 1));
        return users.stream().map(u -> new SimpleEmployeeDTO(u.getId(), u.getUsername())).collect(Collectors.toList());
    }
    
    /**
     * 查询所有的项目主管（项目上级）
     * @return
     */
    public List<Supervisor> getAllSupervisors() {
    	List<Supervisor> allUsers = userMapper.selectAll();
    	List<Supervisor> allSupervisors = new ArrayList<>();
    	for (Supervisor user: allUsers) {
    		if (viewRolePermissionMapper.selectAllPermissionsByUserId(user.getId()).contains(7)) {
    			allSupervisors.add(user);
    		}
    	}
    	return allSupervisors;
    }
    
    /**
     * 查询所有的组织配置管理员
     * @return
     */
    public List<Supervisor> getAllConfigOrganizer() {
    	List<Supervisor> allUsers = userMapper.selectAll();
    	List<Supervisor> allConfigOrganizers = new ArrayList<>();
    	for (Supervisor user: allUsers) {
    		if (viewRolePermissionMapper.selectAllPermissionsByUserId(user.getId()).contains(9)) {
    			allConfigOrganizers.add(user);
    		}
    	}
    	return allConfigOrganizers;
    }
    
    /**
     * 查询所有的QAManager
     * @return
     */
    public List<Supervisor> getAllQAManager() {
    	List<Supervisor> allUsers = userMapper.selectAll();
    	List<Supervisor> allQAManagers = new ArrayList<>();
    	for (Supervisor user: allUsers) {
    		if (viewRolePermissionMapper.selectAllPermissionsByUserId(user.getId()).contains(10)) {
    			allQAManagers.add(user);
    		}
    	}
    	return allQAManagers;
    }
    
    /**
     * 查询所有的EPGLeader
     * @return
     */
    public List<Supervisor> getAllEPGLeader() {
    	List<Supervisor> allUsers = userMapper.selectAll();
    	List<Supervisor> allEPGLeaders = new ArrayList<>();
    	for (Supervisor user: allUsers) {
    		if (viewRolePermissionMapper.selectAllPermissionsByUserId(user.getId()).contains(11)) {
    			allEPGLeaders.add(user);
    		}
    	}
    	return allEPGLeaders;
    }
}
