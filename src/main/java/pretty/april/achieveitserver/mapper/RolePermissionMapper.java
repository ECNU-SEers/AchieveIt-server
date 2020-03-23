package pretty.april.achieveitserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import pretty.april.achieveitserver.entity.RolePermission;

import java.util.List;

@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    @Insert("<script>" +
            "insert into role_permission (role_id,permission_id) values" +
            "<foreach collections='rps' item='rp' open='' separator=',' close='' >" +
            "(#{rp.roleId},#{rp.permissionId})" +
            "</foreach>" +
            "</script>")
    void insertBatch(List<RolePermission> rps);
}
