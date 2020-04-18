package pretty.april.achieveitserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import pretty.april.achieveitserver.entity.ProjectMember;
import pretty.april.achieveitserver.model.Member;
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
    List<ProjectMember> selectByProjectId(@Param("projectId") Integer projectId);
    
    @Select("SELECT * FROM project_member where project_id = #{projectId} and user_id = #{userId};")
    ProjectMember selectByProjectIdAndUserId(@Param("projectId") Integer projectId, @Param("userId") Integer userId);

    /**
     * 更新项目内容时更新成员表中的project_member
     *
     * @param projectName
     * @param projectId
     * @return
     */
    @Update("UPDATE project_member SET project_name = #{projectName} WHERE project_id = #{projectId}")
    int updateProjectNameByUserIdAndProjectId(@Param("projectName") String projectName, @Param("projectId") Integer projectId);

    @Select("select user.id user_id,user.username,real_name,email,department,phone_number,leader_id,leader_name " +
            "from project_member inner join user on project_member.user_id = user.id " +
            "where project_id = #{projectId} and user.real_name like concat('%',#{keyword},'%') limit #{limit} offset #{offset}")
    List<MemberDetails> selectMemberDetailsByProjectIdAndNameKeyword(@Param("projectId") Integer projectId, @Param("keyword") String keyword, @Param("limit") Integer limit, @Param("offset") Integer offset);

    @Select({"select count(*) from project_member where project_id = #{projectId}"})
    Integer selectMemberCountByProjectId(Integer projectId);

    @Select("select user.id user_id,user.username,real_name,email,department,phone_number,leader_id,leader_name " +
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

    @Select("select u.id id,u.username username,u.real_name real_name from project_member pm left join user u on pm.user_id = u.id where project_id = #{projectId} and real_name like concat('%',#{keyword},'%')")
    List<Member> selectByProjectIdAndRealNameLike(@Param("projectId") Integer projectId, @Param("keyword") String keyword);

    @Select("select name from project_member pm left join project p on pm.project_id = p.id where user_id = #{userId} and state = #{state}")
    List<String> selectProjectNamesByUserIdAndState(@Param("userId") Integer userId, @Param("state") String state);

    @Select("select project_id id,name from project_member pm left join project p on pm.project_id = p.id where user_id = #{userId} and state = #{state}")
    List<Searchable> selectProjectIdNameByUserIdAndState(@Param("userId") Integer userId, @Param("state") String state);
}
