package pretty.april.achieveitserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import pretty.april.achieveitserver.entity.UserViewRole;
import pretty.april.achieveitserver.entity.ViewRole;

import java.util.List;

@Mapper
public interface UserViewRoleMapper extends BaseMapper<UserViewRole> {

    @Select("select role_id id,name,remark " +
            "from user_view_role uvr left join view_role vr on uvr.role_id = vr.id " +
            "where user_id = #{userId}")
    List<ViewRole> getViewRolesByUserId(Integer userId);
}
