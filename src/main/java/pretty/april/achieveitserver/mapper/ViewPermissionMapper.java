package pretty.april.achieveitserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import pretty.april.achieveitserver.entity.ViewPermission;

import java.util.List;

@Mapper
public interface ViewPermissionMapper extends BaseMapper<ViewPermission> {

    @Select("select permission_id id,vp.name name,vp.remark remark, module " +
            "from view_role_permission vrp left join view_permission vp on vrp.permission_id = vp.id " +
            "where role_id = #{roleId}")
    List<ViewPermission> selectByRoleId(Integer roleId);
}
