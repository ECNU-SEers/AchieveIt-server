package pretty.april.achieveitserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import pretty.april.achieveitserver.entity.RolePermission;

import java.util.List;

@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    @Insert("<script>" +
            "insert into role_permission (role_id,permission_id) values" +
            "<foreach collection='rps' item='rp' open='' separator=',' close='' >" +
            "(#{rp.roleId},#{rp.permissionId})" +
            "</foreach>" +
            "</script>")
    void insertBatch(@Param("rps") List<RolePermission> rps);
    
    /**
     * 查询某个角色的所有权限
     *
     * @param userId
     * @return 用户权限
     */
    @Select("SELECT permission_id FROM role_permission WHERE role_id = #{roleId}")
    List<Integer> selectByRoleId(Integer roleId);
}
