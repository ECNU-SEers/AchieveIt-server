package pretty.april.achieveitserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import pretty.april.achieveitserver.entity.ViewPermission;
import pretty.april.achieveitserver.entity.ViewRolePermission;

import java.util.List;

@Mapper
public interface ViewRolePermissionMapper extends BaseMapper<ViewRolePermission> {

    @Select("<script>" +
            "select a.permission_id id, module, b.name name " +
            "from view_role_permission a " +
            "left join view_permission b on a.permission_id = b.id " +
            "where a.role_id in " +
            "<foreach collection='roles' item='r' open='(' close=')' separator=','>" +
            "#{r}" +
            "</foreach>" +
            "</script>")
    List<ViewPermission> selectViewPermissionWithModuleByRoleIdIn(@Param("roles") List<Integer> roles);
}
