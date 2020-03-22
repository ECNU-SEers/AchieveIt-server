package pretty.april.achieveitserver.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import pretty.april.achieveitserver.entity.ProjectFunction;

@Mapper
public interface ProjectFunctionMapper extends BaseMapper<ProjectFunction> {

	/**
	 * 利用projectId找到所有该project的功能
	 * @param projectId
	 * @return 所有该project的功能
	 */
	@Select("SELECT * FROM project_function WHERE project_id = #{projectId}")
	List<ProjectFunction> selectProjectFunctionByProjectId(Integer projectId);
}