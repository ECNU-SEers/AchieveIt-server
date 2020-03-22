package pretty.april.achieveitserver.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import pretty.april.achieveitserver.entity.ProjectMember;

@Mapper
public interface ProjectMemberMapper extends BaseMapper<ProjectMember> {

	/**
	 * 查询项目表id为$projectId的项目数
	 * @param projectId
	 * @return
	 */
	@Select("SELECT COUNT(*) FROM project_member WHERE project_id = #{projectId}")
	int selectCountByProjectId(Integer projectId);
}
