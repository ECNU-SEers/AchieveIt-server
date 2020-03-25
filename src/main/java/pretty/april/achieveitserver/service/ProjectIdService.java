package pretty.april.achieveitserver.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import pretty.april.achieveitserver.entity.ProjectId;
import pretty.april.achieveitserver.mapper.ProjectIdMapper;

@Service
public class ProjectIdService extends ServiceImpl<ProjectIdMapper, ProjectId> {

	public List<ProjectId> getAllProjectIds() {
		return this.baseMapper.selectAllProjectId();
	}
}
