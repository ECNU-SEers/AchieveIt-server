package pretty.april.achieveitserver.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import pretty.april.achieveitserver.dto.ConfigDTO;
import pretty.april.achieveitserver.entity.ProjectConfig;
import pretty.april.achieveitserver.mapper.ProjectConfigMapper;
import pretty.april.achieveitserver.request.EditConfigRequest;

@Service
public class ConfigService {

    private ProjectConfigMapper projectConfigMapper;

    public ConfigService(ProjectConfigMapper projectConfigMapper) {
        this.projectConfigMapper = projectConfigMapper;
    }

    public ConfigDTO getConfig(Integer projectId) {
        ProjectConfig config = projectConfigMapper.selectOne(new QueryWrapper<ProjectConfig>()
                .eq("project_id", projectId));
        ConfigDTO configDTO = new ConfigDTO();
        BeanUtils.copyProperties(config, configDTO);
        return configDTO;
    }

    public void editConfig(Integer projectId, EditConfigRequest request) {
        ProjectConfig config = new ProjectConfig();
        config.setProjectId(projectId);
        BeanUtils.copyProperties(request, config);
        ProjectConfig projectConfig = projectConfigMapper.selectOne(new QueryWrapper<ProjectConfig>()
                .eq("project_id", projectId));
        if (projectConfig.getIsFileServerDirConfirmed()) {
            config.setIsFileServerDirConfirmed(null);
        }
        projectConfigMapper.update(config, new QueryWrapper<ProjectConfig>().eq("project_id", projectId));
    }
}
