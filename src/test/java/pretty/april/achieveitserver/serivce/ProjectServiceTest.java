package pretty.april.achieveitserver.serivce;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import pretty.april.achieveitserver.dto.PageDTO;
import pretty.april.achieveitserver.entity.Project;
import pretty.april.achieveitserver.request.project.ApproveProjectRequest;
import pretty.april.achieveitserver.request.project.AssignRoleRequest;
import pretty.april.achieveitserver.request.project.CreateProjectRequest;
import pretty.april.achieveitserver.request.project.ObtainAllProjectRequest;
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

//    @Test
//    public void getAllAllIdAndOuterIdAndNameTest() {
//    	List<ObtainAllProjectRequest> allProjects = projectService.getAllIdAndOuterIdAndName();
//    	System.out.println(allProjects.size());
//    	for (ObtainAllProjectRequest project: allProjects) {
//    		System.out.println("projectId = "+project.getId());
//    		System.out.println("projectOuterId = "+project.getOuterId());
//    		System.out.println("projectName = "+project.getName());
//    		System.out.println("===================");
//    	}
//    }
//    
//	@Test
//	public void createProjectTest() {
//		CreateProjectRequest project = new CreateProjectRequest();
//		List<String> skillNames = new ArrayList<String>();
//		skillNames.add("Java");
//		skillNames.add("MySQL");
//		
//		project.setOuterId("2020-ECNU-D-04");
//		project.setName("AchieveIt");
//		project.setStartDate(LocalDate.now());
//		project.setEndDate(LocalDate.now().plusDays(50));
//		project.setClientOuterId("C01");
//		project.setCompany("阿里巴巴");
//		project.setSupervisorId(3);
//		project.setSupervisorName("SY-2020-03");
//		project.setConfigOrganizerId(20);
//		project.setConfigOrganizerName("configLeader-201709");
//		project.setEpgLeaderId(16);
//		project.setEpgLeaderName("EPGLeader-201711");
//		project.setQaManagerId(18);
//		project.setQaManagerName("QALeader-201810");
//		project.setSkillNames(skillNames);
//		project.setBusinessAreaName("AI");
//		
//		projectService.createProject(project);
//	}
//	
//	@Test
//	public void createProjectTest_exception() {
//		CreateProjectRequest project = new CreateProjectRequest();
//		List<String> skillNames = new ArrayList<String>();
//		skillNames.add("Java");
//		skillNames.add("MySQL");
//		
//		project.setOuterId("2020-ECNU-D-02");
//		project.setName("AchieveIt");
//		project.setStartDate(LocalDate.now());
//		project.setEndDate(LocalDate.now().plusDays(50));
//		project.setClientOuterId("C01");
//		project.setCompany("阿里巴巴");
//		project.setSupervisorId(3);
//		project.setSupervisorName("SY-2020-03");
//		project.setSkillNames(skillNames);
//		project.setBusinessAreaName("AI");
//		
//		projectService.createProject(project);
//	}
//
//	@Test
//	public void retrieveProjectTest() {
//		String outerId = "2020-ECNU-D-03";
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
//	public void updateProjectInfoWithoutSkillsAndBusinessAreaAndMilestoneTest() {
//		UpdateProjectInfoRequest project = new UpdateProjectInfoRequest();
//		
//		project.setOuterId("P01");
//		project.setName("AchieveIt0402");
//		project.setStartDate(LocalDate.now().plusDays(2));
//		project.setEndDate(LocalDate.now().plusDays(45));
//		project.setClientOuterId("C01");
//		project.setCompany("阿里巴巴");
//		projectService.updateProjectInfoWithoutSkillsAndBusinessAreaAndMilestone(project);
//	}
//    
//    @Test
//	public void updateProjectInfoWithoutSkillsAndBusinessAreaAndMilestoneTest_expire() {
//		UpdateProjectInfoRequest project = new UpdateProjectInfoRequest();
//		
//		project.setOuterId("2020-ECNU-D-03");
//		project.setName("AchieveIt0402");
//		project.setStartDate(LocalDate.now().plusDays(2));
//		project.setEndDate(LocalDate.now().plusDays(45));
//		project.setClientOuterId("C01");
//		project.setCompany("阿里巴巴");
//		projectService.updateProjectInfoWithoutSkillsAndBusinessAreaAndMilestone(project);
//	}
//  
//    
//    @Test
//	public void updateProjectTest() {
//		UpdateProjectRequest project = new UpdateProjectRequest();
//		List<String> skillNames = new ArrayList<String>();
//		skillNames.add("Vue.js");
//		skillNames.add("MongoDB");
//		
//		project.setOuterId("2020-ECNU-D-02");
//		project.setName("AchieveIt2");
//		project.setStartDate(LocalDate.now().plusDays(2));
//		project.setEndDate(LocalDate.now().plusDays(45));
//		project.setClientOuterId("C01");
//		project.setCompany("阿里巴巴");
//		project.setSkillNames(skillNames);
//		project.setBusinessAreaName("金融");
//		project.setMilestone("milestone:1st");
//		projectService.updateProjectInfo(project);
//	}
//    
//	@Test
//	public void updateProjectTest_expire() {
//		UpdateProjectRequest project = new UpdateProjectRequest();
//		List<String> skillNames = new ArrayList<String>();
//		skillNames.add("Vue.js");
//		skillNames.add("MongoDB");
//		
//		project.setOuterId("2020-ECNU-D-03");
//		project.setName("AchieveIt2");
//		project.setStartDate(LocalDate.now().plusDays(2));
//		project.setEndDate(LocalDate.now().plusDays(45));
//		project.setClientOuterId("C01");
//		project.setCompany("阿里巴巴");
//		project.setSkillNames(skillNames);
//		project.setBusinessAreaName("金融");
//		project.setMilestone("milestone:1st");
//		projectService.updateProjectInfo(project);
//	}
//
	@Test
	public void updateProjectDuringProjectApprovalProcessTest() {
		UpdateProjectRequest project = new UpdateProjectRequest();
		List<String> skillNames = new ArrayList<String>();
		skillNames.add("Vue.js");
		skillNames.add("MongoDB");
		
		project.setOuterId("2020-ECNU-D-01");
		project.setName("AchieveIt2");
		project.setStartDate(LocalDate.now().plusDays(2));
		project.setEndDate(LocalDate.now().plusDays(45));
		project.setClientOuterId("C01");
		project.setCompany("阿里巴巴");
		project.setSkillNames(skillNames);
		project.setBusinessAreaName("AI");
		project.setMilestone("milestone:1st");
		projectService.updateProjectInfoDuringProjectApproval(project);
	}

//	@Test
//	public void searchProjectTest() {
//		String keyword = "项目";
//		Integer userId = 1;
//		List<Project> projects = projectService.selectProjectByNameWithKeyword(userId, keyword);
//		System.out.println(projects.size());
//		for (Project p: projects) {
//			System.out.println("projectOuterId = " + p.getOuterId());
//			System.out.println("name"+p.getName());
//			System.out.println("------------");
//		}
//		List<SearchProjectRequest> results = projectService.searchProjectWithNameIncludingKeyword(keyword);
//		System.out.println(results.size());
//		for (SearchProjectRequest r: results) {
//			System.out.println("projectOuterId = "+r.getOuterId());
//			System.out.println("name"+r.getName());
//			System.out.println("======================================");
//		}
//	}
//    
//
//    @Test
//    public void retrieveProjectsWithNameIncluingKeywordByPageTest() {
//    	String keyword = "项目";
//    	PageDTO<RetrieveProjectRequest> page = projectService.retrieveProjectsWithNameIncluingKeywordByPage(1, 5, keyword);
//    	System.out.println(page.getTotal());
//    }
//
//	@Test
//	public void showProjectListTest() {
//		String outerId = "2020-ECNU-D-02";
//		ShowProjectListRequest projectList = projectService.showProjectList(outerId);
//		System.out.println("startDate="+projectList.getStartDate());
//		System.out.println("company="+projectList.getCompany());
//		System.out.println("epg="+projectList.getEpgAssigned());
//		System.out.println("counter="+projectList.getParticipantCounter());
//		System.out.println("clientOuterId="+projectList.getClientOuterId());
//	}
//	
//    @Test
//	public void showProjectsTest() {
//    	String keyword = "";
//		PageDTO<ShowProjectListRequest> page = projectService.showProjects(1, 10, keyword);
//		System.out.println("items:\n"+page.getItems());
//		System.out.println("total = "+ page.getTotal());
//		for (ShowProjectListRequest request: page.getItems()) {
//			System.out.println("outerId = "+request.getOuterId());
//			System.out.println("name = "+request.getName());
//			System.out.println("id = "+request.getId());
//		}
//	}    
//
//    @Test
//    public void acceptProjectTest() {
//        String outerId = "2020-ECNU-D-02";
//        String remark = "acceptTest0402";
//        ApproveProjectRequest apd = projectService.acceptProject(outerId, remark);
//        if (apd.isReviewResult()) {
//            System.out.println("yes");
//        }
//    }
//
//	@Test
//	public void rejectProjectTest() {
//		String outerId = "2020-ECNU-D-01";
//		String remark = "rejectTest0329";
//		ApproveProjectRequest apd = projectService.rejectProject(outerId, remark);
//		if (apd.isReviewResult()) {
//			System.out.println("yes");
//		}
//	}
//
	@Test
	public void assignEQGTest_exception() {
		String outerId = "2020-ECNU-D-00";
		AssignRoleRequest arr = new AssignRoleRequest();
		List<Integer> userId = new ArrayList<Integer>();
		userId.add(1);
		arr.setOuterId(outerId);
		arr.setUserId(userId);
		Project project = projectService.assignEPG(arr);
		System.out.println("EPG state = "+project.getEpgAssigned());
	}
	
	@Test
	public void assignEQGTest() {
		String outerId = "2020-ECNU-D-02";
		AssignRoleRequest arr = new AssignRoleRequest();
		List<Integer> userId = new ArrayList<Integer>();
		userId.add(1);
		arr.setOuterId(outerId);
		arr.setUserId(userId);
		Project project = projectService.assignEPG(arr);
		System.out.println("EPG state = "+project.getEpgAssigned());
	}

	@Test
	public void assignQATest_exception() {
		String outerId = "2020-ECNU-D-00";
		AssignRoleRequest arr = new AssignRoleRequest();
		List<Integer> userId = new ArrayList<Integer>();
		userId.add(1);
		arr.setOuterId(outerId);
		arr.setUserId(userId);
		Project project = projectService.assignQA(arr);
		System.out.println("QA state = "+project.getQaAssigned());
	}
	
	@Test
	public void assignQATest() {
		String outerId = "2020-ECNU-D-02";
		AssignRoleRequest arr = new AssignRoleRequest();
		List<Integer> userId = new ArrayList<Integer>();
		userId.add(1);
		arr.setOuterId(outerId);
		arr.setUserId(userId);
		Project project = projectService.assignQA(arr);
		System.out.println("QA state = "+project.getQaAssigned());
	}

	@Test
	public void assignConfigTest_exception() {
		String outerId = "2020-ECNU-D-00";
		Project project = projectService.assignConfig(outerId);
		System.out.println("Config state = "+project.getConfigAssigned());
	}
	
	@Test
	public void assignConfigTest() {
		String outerId = "2020-ECNU-D-02";
		Project project = projectService.assignConfig(outerId);
		System.out.println("Config state = "+project.getConfigAssigned());
	}

	@Test
	public void setConfigInfoTest() {
		String outerId = "2020-ECNU-D-02";
		String remark = "setConfig";
		Project project = projectService.setConfigInfo(outerId, remark);
		System.out.println("State = "+project.getState());
	}
}
