package pretty.april.achieveitserver.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import pretty.april.achieveitserver.entity.ProjectSkill;

@Mapper
public interface ProjectSkillMapper extends BaseMapper<ProjectSkill> {

	/**
	 * 利用projectId找到所有该project采用的技术
	 * @param projectId
	 * @return 所有该project采用的技术
	 */
	@Select("SELECT * FROM project_skill WHERE project_id = #{projectId}")
	List<ProjectSkill> selectProjectSkillByProjectId(Integer projectId);
	
	/**
	 * 利用projectId删除所有该project采用的技术
	 * @param projectId
	 * @return
	 */
	@Delete("DELETE FROM project_skill WHERE project_id = #{projectId}")
	int deleteProjectSkillByProjectId(Integer projectId);
}
