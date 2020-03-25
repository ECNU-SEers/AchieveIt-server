package pretty.april.achieveitserver.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import pretty.april.achieveitserver.entity.ProjectId;

@Mapper
public interface ProjectIdMapper extends BaseMapper<ProjectId> {

	@Select("SELECT * FROM project_id WHERE isfree = true")
	List<ProjectId> selectAllProjectId();
}
