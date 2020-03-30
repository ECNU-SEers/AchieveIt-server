package pretty.april.achieveitserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import pretty.april.achieveitserver.entity.Project;

import java.util.List;

@Mapper
public interface ProjectMapper extends BaseMapper<Project> {

    /**
     * 查询项目ID为$outerId的项目数
     *
     * @param outerId
     * @return 项目数
     */
    @Select("SELECT COUNT(*) FROM project WHERE outer_id = #{outerId}")
    int selectCountByOuterId(String outerId);

    /**
     * 查询项目名称中包含关键字的所有项目
     *
     * @param keyword
     * @return
     */
    @Select("SELECT project.* FROM project, project_member WHERE project_member.user_id = #{userId} AND project.id = project_member.project_id AND project.name LIKE \"%\"#{keyword}\"%\"")
    List<Project> selectByNameLikeKeyword(@Param("userId")Integer userId, @Param("keyword")String keyword, Page<Project> page);
    
    @Select("SELECT project.* FROM project, project_member WHERE project_member.user_id = #{userId} AND project.id = project_member.project_id AND project.name LIKE \"%\"#{keyword}\"%\"")
    List<Project> selectByNameLikeKeyword1(@Param("userId")Integer userId, @Param("keyword")String keyword);
    
    /**
     * 查询QA经理参与的所有项目，可用关键字搜索项目名称
     * 
     * @param userId
     * @return 某个QA经理参与的所有项目
     */
    @Select("SELECT project.* FROM project, project_member WHERE project_member.user_id = #{userId} AND project.id = project_member.project_id AND project.name LIKE \"%\"#{keyword}\"%\" ORDER BY qa_assigned, start_date")
    List<Project> selectProjectsOfQAManager(@Param("userId")Integer userId, @Param("keyword")String keyword, Page<Project> page);
    
    /**
     * 查询EPG_Leader参与的所有项目，可用关键字搜索项目名称
     * 
     * @param userId
     * @return 某个EPG_Leader参与的所有项目
     */
    @Select("SELECT project.* FROM project, project_member WHERE project_member.user_id = #{userId} AND project.id = project_member.project_id AND project.name LIKE \"%\"#{keyword}\"%\" ORDER BY epg_assigned, start_date")
    List<Project> selectProjectsOfEPGLeader(@Param("userId")Integer userId, @Param("keyword")String keyword, Page<Project> page);
    
    /**
     * 查询用户（非QA经理 且 非EPG_Leader）参与的所有项目，可用关键字搜索项目名称
     * 
     * @param userId
     * @return 某个用户参与的所有项目
     */
    @Select("SELECT project.* FROM project, project_member WHERE project_member.user_id = #{userId} AND project.id = project_member.project_id AND project.name LIKE \"%\"#{keyword}\"%\" ORDER BY start_date")
    List<Project> selectProjectsOfAUser(@Param("userId")Integer userId, @Param("keyword")String keyword, Page<Project> page);
}
