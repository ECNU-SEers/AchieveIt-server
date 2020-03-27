package pretty.april.achieveitserver.serivce;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import pretty.april.achieveitserver.dto.PageDTO;
import pretty.april.achieveitserver.entity.Project;
import pretty.april.achieveitserver.request.project.ApproveProjectRequest;
import pretty.april.achieveitserver.request.project.AssignRoleRequest;
import pretty.april.achieveitserver.request.project.CreateProjectRequest;
import pretty.april.achieveitserver.request.project.RetrieveProjectRequest;
import pretty.april.achieveitserver.request.project.SearchProjectRequest;
import pretty.april.achieveitserver.request.project.ShowProjectListRequest;
import pretty.april.achieveitserver.request.project.UpdateProjectInfoRequest;
import pretty.april.achieveitserver.request.project.UpdateProjectRequest;
import pretty.april.achieveitserver.service.ProjectService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;

//	@Test
//	public void createProjectTest() throws Exception {
//		CreateProjectRequest project = new CreateProjectRequest();
//		List<String> skillNames = new ArrayList<String>();
//		skillNames.add("tech1");
//		skillNames.add("tech2");
//		
//		project.setOuterId("2020-0327-D-pp");
//		project.setName("AchieveIt");
//		project.setStartDate(LocalDate.now());
//		project.setEndDate(LocalDate.now().plusDays(50));
//		project.setClientOuterId("20200316test");
//		project.setCompany("April");
//		project.setSupervisorId(4);
//		project.setSupervisorName("zhangsan");
//		project.setManagerId(3);
//		project.setManagerName("lisi");
//		project.setSkillNames(skillNames);
//		project.setBusinessAreaName("area1");
//		
//		projectService.createProject(project);
//	}
//
//	@Test
//	public void retrieveProjectTest() {
//		String outerId = "2020-1234-D-qq";
//		RetrieveProjectRequest rpd = projectService.retrieveProject(outerId);
//		System.out.println("projectClientId = "+rpd.getProject().getClientId());
//		System.out.println("projectClientEmail = "+rpd.getProjectClient().getEmail());
//		System.out.println("projectSkillsSize = "+rpd.getProjectSkills().size());
//		System.out.println("projectBusinessAreaName = "+rpd.getProjectBusinessArea().getBusinessAreaName());
//		System.out.println("projectMilestonesSize = "+rpd.getProjectMilestones().size());
//		System.out.println("projectFunctionsSize = "+rpd.getProjectFunctions().size());
//	}
//
//    @Test
//	public void updateProjectInfoWithoutSkillsAndBusinessAreaAndMilestoneTest() throws Exception {
//		UpdateProjectInfoRequest project = new UpdateProjectInfoRequest();
//		
//		project.setOuterId("2020-1234-D-qq");
//		project.setName("AchieveIt0327");
//		project.setStartDate(LocalDate.now().plusDays(2));
//		project.setEndDate(LocalDate.now().plusDays(45));
//		project.setClientOuterId("20200318test");
//		project.setCompany("March");
//		project.setSupervisorId(5);
//		project.setSupervisorName("wangwu");
//		projectService.updateProjectInfoWithoutSkillsAndBusinessAreaAndMilestone(project);
//	}
//    
//	@Test
//	public void updateProjectTest() throws Exception {
//		UpdateProjectRequest project = new UpdateProjectRequest();
//		List<String> skillNames = new ArrayList<String>();
//		skillNames.add("tech1");
//		skillNames.add("tech3");
//		
//		project.setOuterId("2020-1234-D-qq");
//		project.setName("AchieveIt2");
//		project.setStartDate(LocalDate.now().plusDays(2));
//		project.setEndDate(LocalDate.now().plusDays(45));
//		project.setClientOuterId("20200318test");
//		project.setCompany("March");
//		project.setSupervisorId(5);
//		project.setSupervisorName("wangwu");
//		project.setSkillNames(skillNames);
//		project.setBusinessAreaName("area2");
//		project.setMilestone("milestone:1st");
//		projectService.updateProjectInfo(project);
//	}
//
//	@Test
//	public void updateProjectDuringProjectApprovalProcessTest() throws Exception {
//		UpdateProjectRequest project = new UpdateProjectRequest();
//		List<String> skillNames = new ArrayList<String>();
//		skillNames.add("tech1");
//		skillNames.add("tech3");
//		
//		project.setOuterId("2020-0322-D-pp");
//		project.setName("AchieveIt2");
//		project.setStartDate(LocalDate.now().plusDays(2));
//		project.setEndDate(LocalDate.now().plusDays(45));
//		project.setClientOuterId("20200318test");
//		project.setCompany("March");
//		project.setSupervisorId(5);
//		project.setSupervisorName("wangwu");
//		project.setSkillNames(skillNames);
//		project.setBusinessAreaName("area2");
//		project.setMilestone("milestone:1st");
//		projectService.updateProjectInfoDuringProjectApproval(project);
//	}
//
//	@Test
//	public void searchProjectTest() {
//		String keyword = "ch";
//		Integer userId = 1;
//		List<Project> projects = projectService.selectProjectByNameWithKeyword(userId, keyword);
//		System.out.println(projects.size());
//		List<SearchProjectRequest> results = projectService.searchProjectWithNameIncludingKeyword(userId, keyword);
//		System.out.println(results.size());
//	}
//    
//
//    @Test
//    public void retrieveProjectsWithNameIncluingKeywordByPageTest() {
//    	String keyword = "ch";
//    	Integer userId = 1;
//    	PageDTO<RetrieveProjectRequest> page = projectService.retrieveProjectsWithNameIncluingKeywordByPage(1, 5, userId, keyword);
//    	System.out.println(page.getTotal());
//    }
//
//	@Test
//	public void showProjectListTest() {
//		String outerId = "2020-1234-D-qq";
//		ShowProjectListRequest projectList = projectService.showProjectList(outerId);
//		System.out.println("startDate="+projectList.getStartDate());
//		System.out.println("company="+projectList.getCompany());
//		System.out.println("epg="+projectList.getEpgAssigned());
//		System.out.println("counter="+projectList.getParticipantCounter());
//		System.out.println("clientOuterId="+projectList.getClientOuterId());
//	}
//	
    @Test
	public void showProjectsTest() {
		PageDTO<ShowProjectListRequest> page = projectService.showProjects(1, 5, 3);
		System.out.println("items:\n"+page.getItems());
		System.out.println("total = "+ page.getTotal());
		for (ShowProjectListRequest request: page.getItems()) {
			System.out.println("outerId = "+request.getOuterId());
			System.out.println("name = "+request.getName());
		}
		
	}
//
//    @Test
//    public void acceptProjectTest() throws Exception {
//        String outerId = "2020-0322-D-pp";
//        RetrieveProjectRequest rpd = projectService.retrieveProject(outerId);
//        rpd.getProject().setRemark("0322good job!");
//        ApproveProjectRequest apd = projectService.acceptProject(rpd);
//        System.out.println("state = " + apd.getProjectInfo().getProject().getState());
//        if (apd.isReviewResult()) {
//            System.out.println("yes");
//        }
//    }
//
//	@Test
//	public void rejectProjectTest() throws Exception {
//		String outerId = "2020-0322-D-pp";
//		RetrieveProjectRequest rpd = projectService.retrieveProject(outerId);
//		rpd.getProject().setRemark("0322bad job!");
//		ApproveProjectRequest apd = projectService.rejectProject(rpd);
//		System.out.println("state = "+apd.getProjectInfo().getProject().getState());
//		if (apd.isReviewResult()) {
//			System.out.println("yes");
//		}
//	}
//
//	@Test
//	public void assignEQGTest() {
//		String outerId = "2020-0322-D-qq";
//		AssignRoleRequest arr = new AssignRoleRequest();
//		List<Integer> userId = new ArrayList<Integer>();
//		userId.add(1);
//		arr.setOuterId(outerId);
//		arr.setUserId(userId);
//		Project project = projectService.assignEPG(arr);
//		System.out.println("EPG state = "+project.getEpgAssigned());
//	}
//
//	@Test
//	public void assignQATest() {
//		String outerId = "2020-0322-D-qq";
//		AssignRoleRequest arr = new AssignRoleRequest();
//		List<Integer> userId = new ArrayList<Integer>();
//		userId.add(1);
//		arr.setOuterId(outerId);
//		arr.setUserId(userId);
//		Project project = projectService.assignQA(arr);
//		System.out.println("QA state = "+project.getQaAssigned());
//	}
//
//	@Test
//	public void assignConfigTest() {
//		String outerId = "2020-0322-D-qq";
//		Project project = projectService.assignConfig(outerId);
//		System.out.println("Config state = "+project.getConfigAssigned());
//	}
//
//	@Test
//	public void setConfigInfoTest() {
//		String outerId = "2020-0322-D-qq";
//		Project project = projectService.setConfigInfo(outerId);
//		System.out.println("State = "+project.getState());
//	}
}
