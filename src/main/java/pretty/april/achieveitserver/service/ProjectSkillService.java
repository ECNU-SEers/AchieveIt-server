package pretty.april.achieveitserver.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import pretty.april.achieveitserver.entity.ProjectSkill;
import pretty.april.achieveitserver.mapper.ProjectSkillMapper;

@Service
public class ProjectSkillService extends ServiceImpl<ProjectSkillMapper, ProjectSkill> {

	/**
	 * 通过project_id找到项目使用的所有技术
	 * @param projectId
	 * @return 项目所有的技术
	 */
	public List<ProjectSkill> getProjectSkillByProjectId(Integer projectId) {
		return this.baseMapper.selectProjectSkillByProjectId(projectId);
	}

	/**
	 * 通过project_id删除项目使用的所有技术
	 * @param projectId
	 * @return 
	 */
	public int deleteProjectSkillByProjectId(Integer projectId) {
		return this.baseMapper.deleteProjectSkillByProjectId(projectId);
	}
}
