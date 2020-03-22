package pretty.april.achieveitserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import pretty.april.achieveitserver.entity.UserSysRole;

import java.util.List;

@Mapper
public interface UserSysRoleMapper extends BaseMapper<UserSysRole> {

    @Select("select sys_role.name from user_sys_role inner join sys_role " +
            "on user_sys_role.sys_role_id = sys_role.id " +
            "where user_sys_role.user_id = #{userId}")
    List<String> selectRoleNamesByUserId(int userId);

    @Select("select permission.name from user_role inner join role inner join role_permission inner join permission " +
            "on user_role.role_id = role.id and role_permission.role_id = user_role.role_id " +
            "where user_role.user_id = #{userId}")
    List<String> selectPermissionsByUserId(int userId);
}
