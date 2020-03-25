package pretty.april.achieveitserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import pretty.april.achieveitserver.entity.ViewRolePermission;
import pretty.april.achieveitserver.model.ViewModulePermission;

import java.util.List;

@Mapper
public interface ViewRolePermissionMapper extends BaseMapper<ViewRolePermission> {

    @Select("<script>" +
            "select a.permission_id permission_id, c.name module, b.name name " +
            "from view_role_permission a " +
            "left join view_permission b on a.permission_id = b.id " +
            "left join view_permission_module c on b.module = c.id " +
            "where a.role_id in " +
            "<foreach collection='roles' item='r' open='(' close=')' separator=','>" +
            "#{r}" +
            "</foreach>" +
            "</script>")
    List<ViewModulePermission> selectViewPermissionWithModuleByRoleIdIn(@Param("roles") List<Integer> roles);
}
