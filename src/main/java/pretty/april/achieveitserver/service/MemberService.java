package pretty.april.achieveitserver.service;

import org.springframework.stereotype.Service;
import pretty.april.achieveitserver.entity.Project;
import pretty.april.achieveitserver.entity.ProjectMember;
import pretty.april.achieveitserver.mapper.ProjectMapper;
import pretty.april.achieveitserver.mapper.ProjectMemberMapper;
import pretty.april.achieveitserver.request.AddProjectMemberRequest;

@Service
public class MemberService {

    private ProjectMemberMapper projectMemberMapper;

    private ProjectMapper projectMapper;

    public void addProjectMember(AddProjectMemberRequest request, Integer projectId) {
        Project project = projectMapper.selectById(projectId);
        if (project == null) {
            throw new IllegalArgumentException("Cannot find project");
        }
        ProjectMember projectMember = new ProjectMember();
        projectMember.setProjectId(projectId);
        projectMember.setProjectName(project.getName());
        projectMember.setUserId(request.getUserId());
        projectMember.setUsername(request.getUsername());
        projectMember.setLeaderId(request.getLeaderId());
        projectMember.setLeaderName(request.getLeaderName());

    }
}
