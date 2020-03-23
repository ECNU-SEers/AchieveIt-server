package pretty.april.achieveitserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import pretty.april.achieveitserver.entity.ProjectMember;

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
     * 
     * @param userId
     * @return
     */
    @Select("SELECT project_id FROM project_member WHERE user_id = #{userId}")
    List<Integer> selectProjectIdByUserId(Integer userId);
}
