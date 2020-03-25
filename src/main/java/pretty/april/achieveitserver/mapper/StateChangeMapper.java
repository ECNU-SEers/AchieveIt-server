package pretty.april.achieveitserver.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import pretty.april.achieveitserver.entity.StateChange;

@Mapper
public interface StateChangeMapper extends BaseMapper<StateChange> {

	/**
	 * 查询某个项目的所有状态变化记录
	 * @param projectId
	 * @return 某个项目的所有状态变化记录
	 */
	@Select("SELECT * FROM state_change WHERE project_id = #{projectId}")
	List<StateChange> selectByProjectId(Integer projectId);
}
