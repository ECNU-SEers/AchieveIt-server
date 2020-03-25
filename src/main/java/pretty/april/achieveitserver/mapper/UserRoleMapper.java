package pretty.april.achieveitserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import pretty.april.achieveitserver.entity.UserRole;

import java.util.List;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    @Insert("<script>" +
            "insert into user_role (user_id,project_id,role_id) values" +
            "<foreach collection='userRoles' item='ur' open='' separator=',' close='' >" +
            "(#{ur.userId},#{ur.projectId},#{ur.roleId})" +
            "</foreach>" +
            "</script>")
    void insertBatch(@Param("userRoles") List<UserRole> userRoles);

    @Select("select role.name from " +
            "user_role inner join role on user_role.role_id = role.id " +
            "where user_role.user_id = #{userId} and user_role.project_id = #{projectId}")
    List<String> selectRoleNamesByUserIdAndProjectId(@Param("userId") Integer userId, @Param("projectId") Integer projectId);

    @Delete("<script>" +
            "delete from user_role where user_id = #{userId} and project_id = #{projectId} and role_id in " +
            "<foreach collection='roles' item='r' open='(' separator=',' close=')' >" +
            "#{r}" +
            "</foreach>" +
            "</script>")
    void deleteBatch(@Param("userId") Integer userId, @Param("projectId") Integer projectId, @Param("roles") List<Integer> roles);

    /**
     * 查询某个用户的所有角色
     *
     * @param userId
     * @return 用户角色
     */
    @Select("SELECT role_id FROM user_role WHERE user_id = #{userId}")
    List<Integer> selectByUserId(Integer userId);
}
