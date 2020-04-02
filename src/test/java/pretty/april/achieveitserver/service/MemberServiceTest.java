package pretty.april.achieveitserver.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import pretty.april.achieveitserver.entity.Project;
import pretty.april.achieveitserver.mapper.ProjectMapper;
import pretty.april.achieveitserver.request.AddProjectMemberRequest;
import pretty.april.achieveitserver.request.EditMemberRequest;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private ProjectMapper projectMapper;

    @Test
    void addProjectMember() {
        Project project = new Project();
        project.setName("test");
        project.setState("进行中");
        project.setClientId(1);
        project.setRemark("test");
        project.setManagerId(1);
        project.setSupervisorId(1);
        projectMapper.insert(project);
        Integer projectId = project.getId();
        assertNotNull(projectId);
        int memberId = 1;
        AddProjectMemberRequest addProjectMemberRequest = new AddProjectMemberRequest();
        addProjectMemberRequest.setRoleId(Collections.singletonList(1));
        addProjectMemberRequest.setUserId(memberId);
        addProjectMemberRequest.setUsername("admin");
        addProjectMemberRequest.setLeaderId(memberId);
        addProjectMemberRequest.setLeaderName("admin");
        memberService.addProjectMember(addProjectMemberRequest, projectId);
    }

    @Test
    void getMembers() {
        Project project = new Project();
        project.setName("test");
        project.setState("进行中");
        project.setClientId(1);
        project.setRemark("test");
        project.setManagerId(1);
        project.setSupervisorId(1);
        projectMapper.insert(project);
        Integer projectId = project.getId();
        assertNotNull(projectId);
        int memberId = 1;
        AddProjectMemberRequest addProjectMemberRequest = new AddProjectMemberRequest();
        addProjectMemberRequest.setRoleId(Collections.singletonList(1));
        addProjectMemberRequest.setUserId(memberId);
        addProjectMemberRequest.setUsername("admin");
        addProjectMemberRequest.setLeaderId(memberId);
        addProjectMemberRequest.setLeaderName("admin");
        memberService.addProjectMember(addProjectMemberRequest, projectId);
        String keyword = "";
        assertNotNull(memberService.getMembers(1, 5, projectId, keyword));
    }

    @Test
    void getMember() {
        Project project = new Project();
        project.setName("test");
        project.setState("进行中");
        project.setClientId(1);
        project.setRemark("test");
        project.setManagerId(1);
        project.setSupervisorId(1);
        projectMapper.insert(project);
        Integer projectId = project.getId();
        assertNotNull(projectId);
        int memberId = 1;
        AddProjectMemberRequest addProjectMemberRequest = new AddProjectMemberRequest();
        addProjectMemberRequest.setRoleId(Collections.singletonList(1));
        addProjectMemberRequest.setUserId(memberId);
        addProjectMemberRequest.setUsername("admin");
        addProjectMemberRequest.setLeaderId(memberId);
        addProjectMemberRequest.setLeaderName("admin");
        memberService.addProjectMember(addProjectMemberRequest, projectId);
        assertNotNull(memberService.getMember(projectId, memberId));
    }

    @Test
    void editMember() {
        Project project = new Project();
        project.setName("test");
        project.setState("进行中");
        project.setClientId(1);
        project.setRemark("test");
        project.setManagerId(1);
        project.setSupervisorId(1);
        projectMapper.insert(project);
        Integer projectId = project.getId();
        assertNotNull(projectId);
        int memberId = 1;
        AddProjectMemberRequest addProjectMemberRequest = new AddProjectMemberRequest();
        addProjectMemberRequest.setRoleId(Collections.singletonList(1));
        addProjectMemberRequest.setUserId(memberId);
        addProjectMemberRequest.setUsername("admin");
        addProjectMemberRequest.setLeaderId(memberId);
        addProjectMemberRequest.setLeaderName("admin");
        memberService.addProjectMember(addProjectMemberRequest, projectId);
        assertNotNull(memberService.getMember(projectId, memberId));
        EditMemberRequest editMemberRequest = new EditMemberRequest();
        editMemberRequest.setRoles(new ArrayList<>());
        memberService.editMember(memberId, projectId, editMemberRequest);
    }

    @Test
    void deleteMember() {
        Project project = new Project();
        project.setName("test");
        project.setState("进行中");
        project.setClientId(1);
        project.setRemark("test");
        project.setManagerId(1);
        project.setSupervisorId(1);
        projectMapper.insert(project);
        Integer projectId = project.getId();
        assertNotNull(projectId);
        int memberId = 1;
        AddProjectMemberRequest addProjectMemberRequest = new AddProjectMemberRequest();
        addProjectMemberRequest.setRoleId(Collections.singletonList(1));
        addProjectMemberRequest.setUserId(memberId);
        addProjectMemberRequest.setUsername("admin");
        addProjectMemberRequest.setLeaderId(memberId);
        addProjectMemberRequest.setLeaderName("admin");
        memberService.addProjectMember(addProjectMemberRequest, projectId);
        memberService.deleteMember(memberId, projectId);
    }

    @Test
    void searchMembers() {
        Project project = new Project();
        project.setName("test");
        project.setState("进行中");
        project.setClientId(1);
        project.setRemark("test");
        project.setManagerId(1);
        project.setSupervisorId(1);
        projectMapper.insert(project);
        Integer projectId = project.getId();
        assertNotNull(projectId);
        int memberId = 1;
        AddProjectMemberRequest addProjectMemberRequest = new AddProjectMemberRequest();
        addProjectMemberRequest.setRoleId(Collections.singletonList(1));
        addProjectMemberRequest.setUserId(memberId);
        addProjectMemberRequest.setUsername("admin");
        addProjectMemberRequest.setLeaderId(memberId);
        addProjectMemberRequest.setLeaderName("admin");
        memberService.addProjectMember(addProjectMemberRequest, projectId);
        String keyword = "";
        assertNotNull(memberService.searchMembers(projectId, keyword));
    }
}