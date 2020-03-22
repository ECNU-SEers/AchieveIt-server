package pretty.april.achieveitserver.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import pretty.april.achieveitserver.entity.ProjectBusinessArea;
import pretty.april.achieveitserver.mapper.ProjectBusinessAreaMapper;

@Service
public class ProjectBusinessAreaService extends ServiceImpl<ProjectBusinessAreaMapper, ProjectBusinessArea> {

	/**
	 * 通过project_id找到项目的业务领域
	 * @param projectId
	 * @return 项目的业务领域
	 */
	public ProjectBusinessArea getProjectBusinessAreaByProjectId(Integer projectId) {
		return this.baseMapper.selectProjectBusinessAreaByProjectId(projectId);
	}

	/**
	 * 通过project_id删除项目的业务领域
	 * @param projectId
	 * @return 
	 */
	public int deleteProjectBusinessAreaByProjectId(Integer projectId) {
		return this.baseMapper.deleteProjectBusinessAreaByProjectId(projectId);
	}
}
