package pretty.april.achieveitserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import pretty.april.achieveitserver.entity.User;
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
    List<Username> selectUsernameBatch(@Param("ids") List<Integer> ids);
}
