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
}
