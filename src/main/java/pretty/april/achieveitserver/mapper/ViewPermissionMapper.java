package pretty.april.achieveitserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import pretty.april.achieveitserver.entity.ViewPermission;
import pretty.april.achieveitserver.model.ViewModulePermission;

import java.util.List;

@Mapper
public interface ViewPermissionMapper extends BaseMapper<ViewPermission> {

    @Select("select a.id permission_id,a.name,b.name module from view_permission a left join view_permission_module b on a.module = b.id;")
    List<ViewModulePermission> selectAllViewPermissionsWithModule();
}
