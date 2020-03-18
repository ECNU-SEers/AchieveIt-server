package pretty.april.achieveitserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import pretty.april.achieveitserver.entity.User;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select role.name from user_role inner join role on user_role.role_id = role.id where user_role.user_id = #{userId}")
    List<String> selectRoleNamesByUserId(int userId);
}
