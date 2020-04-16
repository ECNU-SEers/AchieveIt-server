package pretty.april.achieveitserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

    @Select("select distinct pm.user_id id " +
            "from project_member pm left join user_view_role uvr on pm.user_id = uvr.user_id " +
            "left join view_role_permission vrp on uvr.role_id = vrp.role_id " +
            "where pm.project_id = #{projectId} and vrp.permission_id = #{permissionId}")
    List<Integer> selectUserIdByProjectIdAndViewPermissionId(@Param("projectId") Integer projectId, @Param("permissionId") Integer permissionId);

}
