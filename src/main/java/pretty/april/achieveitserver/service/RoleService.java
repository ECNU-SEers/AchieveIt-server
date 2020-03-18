package pretty.april.achieveitserver.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import pretty.april.achieveitserver.entity.Role;
import pretty.april.achieveitserver.mapper.RoleMapper;

@Service
public class RoleService {

    private RoleMapper roleMapper;

    public RoleService(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    public Role getByRoleName(String name) {
        return roleMapper.selectOne(new QueryWrapper<Role>().eq("name", "ROLE_ADMIN"));
    }

    public void insert(Role role) {
        roleMapper.insert(role);
    }
}
