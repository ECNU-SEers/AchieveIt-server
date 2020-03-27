package pretty.april.achieveitserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import pretty.april.achieveitserver.entity.User;
import pretty.april.achieveitserver.model.Supervisor;
import pretty.april.achieveitserver.model.Username;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("<script>" +
            "select id,username from user where id in " +
            "<foreach collection='ids' item='id' open='(' close=')' separator=','>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    List<Username> selectUsernameBatch(List<Integer> ids);
    
    @Select("SELECT id, username, real_name FROM user WHERE position = \"主管\"")
    List<Supervisor> selectUsernameByPosition();

//    List<Username> selectUsernameBatch(@Param("ids") List<Integer> ids);

}
