package pretty.april.achieveitserver.service;

import org.springframework.stereotype.Service;
import pretty.april.achieveitserver.mapper.ProjectMapper;
import pretty.april.achieveitserver.mapper.ProjectMemberMapper;
import pretty.april.achieveitserver.request.AddProjectMemberRequest;

@Service
public class MemberService {

    private ProjectMemberMapper projectMemberMapper;

    private ProjectMapper projectMapper;

    public void addProjectMember(AddProjectMemberRequest request, Integer projectId) {
        if (projectMapper.selectById(projectId) == null) {
            throw new IllegalArgumentException("Cannot find project");
        }

    }
}
