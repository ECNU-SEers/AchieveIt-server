package pretty.april.achieveitserver.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import pretty.april.achieveitserver.entity.ProjectBusinessArea;

@Mapper
public interface ProjectBusinessAreaMapper extends BaseMapper<ProjectBusinessArea> {

	/**
	 * 利用projectId找到所有该project的业务领域
	 * @param projectId
	 * @return 该project的业务领域
	 */
	@Select("SELECT * FROM project_business_area WHERE project_id = #{projectId}")
	ProjectBusinessArea selectProjectBusinessAreaByProjectId(Integer projectId);
	
	/**
	 * 利用projectId删除该project的业务领域
	 * @param projectId
	 * @return
	 */
	@Delete("DELETE FROM project_business_area WHERE project_id = #{projectId}")
	int deleteProjectBusinessAreaByProjectId(Integer projectId);
}
