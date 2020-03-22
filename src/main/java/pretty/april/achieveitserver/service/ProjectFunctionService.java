package pretty.april.achieveitserver.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import pretty.april.achieveitserver.entity.ProjectFunction;
import pretty.april.achieveitserver.mapper.ProjectFunctionMapper;

@Service
public class ProjectFunctionService extends ServiceImpl<ProjectFunctionMapper, ProjectFunction> {

	/**
	 * 通过project_id找到项目的所有功能
	 * @param projectId
	 * @return 项目所有的功能
	 */
	public List<ProjectFunction> getProjectFunctionByProjectId(Integer projectId) {
		return this.baseMapper.selectProjectFunctionByProjectId(projectId);
	}
}
