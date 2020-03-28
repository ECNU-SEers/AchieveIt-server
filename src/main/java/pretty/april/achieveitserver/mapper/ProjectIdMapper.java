package pretty.april.achieveitserver.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import pretty.april.achieveitserver.entity.ProjectId;

@Mapper
public interface ProjectIdMapper extends BaseMapper<ProjectId> {

	@Select("SELECT outer_id FROM project_id WHERE isfree = true")
	List<String> selectAllProjectId();
	
	@Update("UPDATE project_id SET isfree = false WHERE outer_id = #{projectId}")
	int updateIsFreeByProjectId(String projectId);
}
