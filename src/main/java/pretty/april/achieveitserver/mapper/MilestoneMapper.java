package pretty.april.achieveitserver.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import pretty.april.achieveitserver.entity.Milestone;

@Mapper
public interface MilestoneMapper extends BaseMapper<Milestone> {

	/**
	 * 利用projectId找到所有该project的里程碑
	 * @param projectId
	 * @return 所有该project的里程碑
	 */
	@Select("SELECT * FROM milestone WHERE project_id = #{projectId}")
	List<Milestone> selectProjectMilestoneByProjectId(Integer projectId);
}
