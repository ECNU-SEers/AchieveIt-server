package pretty.april.achieveitserver.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import pretty.april.achieveitserver.entity.Milestone;
import pretty.april.achieveitserver.mapper.MilestoneMapper;

@Service
public class MilestoneService extends ServiceImpl<MilestoneMapper, Milestone> {

	/**
	 * 通过project_id找到项目的里程碑
	 * @param projectId
	 * @return 项目的所有里程碑
	 */
	public List<Milestone> getProjectMilestoneByProjectId(Integer projectId) {
		List<Milestone> projectMilestones = this.baseMapper.selectProjectMilestoneByProjectId(projectId);
		return projectMilestones;
	}
}
