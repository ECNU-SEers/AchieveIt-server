package pretty.april.achieveitserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import pretty.april.achieveitserver.entity.ProjectMember;
import pretty.april.achieveitserver.model.MemberDetails;
import pretty.april.achieveitserver.model.Searchable;

import java.util.List;

@Mapper
public interface ProjectMemberMapper extends BaseMapper<ProjectMember> {

    /**
     * 查询项目表id为$projectId的项目数
     *
     * @param projectId
     * @return
     */
    @Select("SELECT COUNT(*) FROM project_member WHERE project_id = #{projectId}")
    int selectCountByProjectId(Integer projectId);

    /**
     * 查询项目表id为$projectId的所有记录
     *
     * @param projectId
     * @return
     */
    @Select("SELECT * FROM project_member WHERE project_id = #{projectId}")
    List<ProjectMember> selectByProjectId(Integer projectId);

    @Select("select user.id,user.username,real_name,email,department,phone_number,leader_id,leader_name " +
            "from project_member inner join user on project_member.user_id = user.id " +
            "where project_id = #{projectId} order by user.username limit #{limit} offset #{offset}")
    List<MemberDetails> selectMemberDetailsByProjectId(@Param("projectId") Integer projectId, @Param("limit") Integer limit, @Param("offset") Integer offset);

    @Select({"select count(*) from project_member where project_id = #{projectId}"})
    Integer selectMemberCountByProjectId(Integer projectId);

    @Select("select user.id,user.username,real_name,email,department,phone_number,leader_id,leader_name " +
            "from project_member inner join user on project_member.user_id = user.id " +
            "where project_member.project_id = #{projectId} and project_member.user_id = #{memberId} ")
    MemberDetails selectMemberDetailsByProjectIdAndMemberId(@Param("projectId") Integer projectId, @Param("memberId") Integer memberId);

    /**
     * @param userId
     * @return
     */
    @Select("SELECT project_id FROM project_member WHERE user_id = #{userId}")
    List<Integer> selectProjectIdByUserId(Integer userId);

    @Select("select user_id id,username name from project_member where project_id = #{projectId} and username like concat('%',#{name},'%')")
    List<Searchable> selectLikeName(@Param("projectId") Integer projectId, @Param("name") String name);
}
