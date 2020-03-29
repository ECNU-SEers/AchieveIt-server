package pretty.april.achieveitserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import pretty.april.achieveitserver.entity.Permission;

import java.util.List;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    @Select("select permission_id id, name, module, remark " +
            "from role_permission left join permission p on role_permission.permission_id = p.id " +
            "where role_id = #{roleId}")
    List<Permission> getByRoleId(Integer roleId);

    @Select("select distinct p.id id,p.name name,p.module module,p.remark remark " +
            "from user_role ur " +
            "left join role_permission rp on ur.role_id = rp.role_id " +
            "left join permission p on rp.permission_id = p.id " +
            "where ur.user_id = #{userId} and ur.project_id = #{projectId}")
    List<Permission> getByUserIdAndProjectId(@Param("userId") Integer userId, @Param("projectId") Integer projectId);
}
