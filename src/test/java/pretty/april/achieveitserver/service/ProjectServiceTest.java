package pretty.april.achieveitserver.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

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
import pretty.april.achieveitserver.security.UserContext;
import pretty.april.achieveitserver.service.ProjectService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;

    @Test
    public void getAllAllIdAndOuterIdAndNameTest() {
    	List<ObtainAllProjectRequest> allProjects = projectService.getAllIdAndOuterIdAndName();
    	System.out.println(allProjects.size());
    	for (ObtainAllProjectRequest project: allProjects) {
    		System.out.println("projectId = "+project.getId());
    		System.out.println("projectOuterId = "+project.getOuterId());
    		System.out.println("projectName = "+project.getName());
    		System.out.println("===================");
    	}
    }
    
	@Test
	public void createProjectTest() {
		CreateProjectRequest project = new CreateProjectRequest();
		List<String> skillNames = new ArrayList<String>();
		skillNames.add("Java");
		skillNames.add("MySQL");
		
		project.setOuterId("2020-ECNU-D-04");
		project.setName("AchieveIt");
		project.setStartDate(LocalDate.now());
		project.setEndDate(LocalDate.now().plusDays(50));
		project.setClientOuterId("C01");
		project.setCompany("阿里巴巴");
		project.setSupervisorId(3);
		project.setSupervisorName("SY-2020-03");
		project.setConfigOrganizerId(20);
		project.setConfigOrganizerName("configLeader-201709");
		project.setEpgLeaderId(16);
		project.setEpgLeaderName("EPGLeader-201711");
		project.setQaManagerId(18);
		project.setQaManagerName("QALeader-201810");
		project.setSkillNames(skillNames);
		project.setBusinessAreaName("AI");
		
		UserContext userContext = new UserContext(1, "admin");
		projectService.createProject(project, userContext);
	}
	
	@Test
	public void createProjectTest_exception() {
		CreateProjectRequest project = new CreateProjectRequest();
		List<String> skillNames = new ArrayList<String>();
		skillNames.add("Java");
		skillNames.add("MySQL");
		
		project.setOuterId("2020-ECNU-D-02");
		project.setName("AchieveIt");
		project.setStartDate(LocalDate.now());
		project.setEndDate(LocalDate.now().plusDays(50));
		project.setClientOuterId("C01");
		project.setCompany("阿里巴巴");
		project.setSupervisorId(3);
		project.setSupervisorName("SY-2020-03");
		project.setSkillNames(skillNames);
		project.setBusinessAreaName("AI");
		
		UserContext userContext = new UserContext(1, "admin");
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			projectService.createProject(project, userContext);
		});
	    assertEquals("The project already exists, please choose a new one.", exception.getMessage());
		
	}

	@Test
	public void retrieveProjectTest() {
		String outerId = "2020-ECNU-D-03";
		RetrieveProjectRequest rpd = projectService.retrieveProject(outerId);
		System.out.println("projectClientId = "+rpd.getProject().getClientId());
		System.out.println("projectClientEmail = "+rpd.getProjectClient().getEmail());
		System.out.println("projectSkillsSize = "+rpd.getProjectSkills().size());
		System.out.println("projectBusinessAreaName = "+rpd.getProjectBusinessArea().getBusinessAreaName());
		System.out.println("projectMilestonesSize = "+rpd.getProjectMilestones().size());
		System.out.println("projectFunctionsSize = "+rpd.getProjectFunctions().size());
	}

    @Test
	public void updateProjectInfoWithoutSkillsAndBusinessAreaAndMilestoneTest() {
		UpdateProjectInfoRequest project = new UpdateProjectInfoRequest();
		
		project.setOuterId("P01");
		project.setName("AchieveIt0402");
		project.setStartDate(LocalDate.now().plusDays(2));
		project.setEndDate(LocalDate.now().plusDays(45));
		project.setClientOuterId("C01");
		project.setCompany("阿里巴巴");
		projectService.updateProjectInfoWithoutSkillsAndBusinessAreaAndMilestone(project);
	}
    
    @Test
	public void updateProjectInfoWithoutSkillsAndBusinessAreaAndMilestoneTest_expire() {
		UpdateProjectInfoRequest project = new UpdateProjectInfoRequest();
		
		project.setOuterId("2020-ECNU-D-03");
		project.setName("AchieveIt0402");
		project.setStartDate(LocalDate.now().plusDays(2));
		project.setEndDate(LocalDate.now().plusDays(45));
		project.setClientOuterId("C01");
		project.setCompany("阿里巴巴");
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			projectService.updateProjectInfoWithoutSkillsAndBusinessAreaAndMilestone(project);
		});
	    assertEquals("The project already expires, cannot be updated, please choose a new one.", exception.getMessage());
	}
  
    
    @Test
	public void updateProjectTest() {
		UpdateProjectRequest project = new UpdateProjectRequest();
		List<String> skillNames = new ArrayList<String>();
		skillNames.add("Vue.js");
		skillNames.add("MongoDB");
		
		project.setOuterId("2020-ECNU-D-02");
		project.setName("AchieveIt2");
		project.setStartDate(LocalDate.now().plusDays(2));
		project.setEndDate(LocalDate.now().plusDays(45));
		project.setClientOuterId("C01");
		project.setCompany("阿里巴巴");
		project.setSkillNames(skillNames);
		project.setBusinessAreaName("金融");
		project.setMilestone("milestone:1st");
		Integer userId = new Integer(1);
		projectService.updateProjectInfo(project, userId);
	}
    
	@Test
	public void updateProjectTest_expire() {
		UpdateProjectRequest project = new UpdateProjectRequest();
		List<String> skillNames = new ArrayList<String>();
		skillNames.add("Vue.js");
		skillNames.add("MongoDB");
		
		project.setOuterId("2020-ECNU-D-03");
		project.setName("AchieveIt2");
		project.setStartDate(LocalDate.now().plusDays(2));
		project.setEndDate(LocalDate.now().plusDays(45));
		project.setClientOuterId("C01");
		project.setCompany("阿里巴巴");
		project.setSkillNames(skillNames);
		project.setBusinessAreaName("金融");
		project.setMilestone("milestone:1st");
		Integer userId = new Integer(1);
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			projectService.updateProjectInfo(project, userId);
		});
	    assertEquals("The project already expires, cannot be updated, please choose a new one.", exception.getMessage());
	}

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
		Integer userId = new Integer(1);
		projectService.updateProjectInfoDuringProjectApproval(project, userId);
	}

	@Test
	public void searchProjectTest() {
		String keyword = "项目";
		Integer userId = new Integer(1);
		List<Project> projects = projectService.selectProjectByNameWithKeyword(userId, keyword);
		System.out.println(projects.size());
		for (Project p: projects) {
			System.out.println("projectOuterId = " + p.getOuterId());
			System.out.println("name"+p.getName());
			System.out.println("------------");
		}
		List<SearchProjectRequest> results = projectService.searchProjectWithNameIncludingKeyword(keyword, userId);
		System.out.println(results.size());
		for (SearchProjectRequest r: results) {
			System.out.println("projectOuterId = "+r.getOuterId());
			System.out.println("name"+r.getName());
			System.out.println("======================================");
		}
	}
    

    @Test
    public void retrieveProjectsWithNameIncluingKeywordByPageTest() {
    	String keyword = "项目";
    	Integer userId = new Integer(1);
    	PageDTO<RetrieveProjectRequest> page = projectService.retrieveProjectsWithNameIncluingKeywordByPage(1, 5, keyword, userId);
    	System.out.println(page.getTotal());
    }

	@Test
	public void showProjectListTest() {
		String outerId = "2020-ECNU-D-02";
		ShowProjectListRequest projectList = projectService.showProjectList(outerId);
		System.out.println("startDate="+projectList.getStartDate());
		System.out.println("company="+projectList.getCompany());
		System.out.println("epg="+projectList.getEpgAssigned());
		System.out.println("counter="+projectList.getParticipantCounter());
		System.out.println("clientOuterId="+projectList.getClientOuterId());
	}
	
    @Test
	public void showProjectsTest() {
    	String keyword = "";
    	Integer userId = new Integer(1);
		PageDTO<ShowProjectListRequest> page = projectService.showProjects(1, 10, keyword, userId);
		System.out.println("items:\n"+page.getItems());
		System.out.println("total = "+ page.getTotal());
		for (ShowProjectListRequest request: page.getItems()) {
			System.out.println("outerId = "+request.getOuterId());
			System.out.println("name = "+request.getName());
			System.out.println("id = "+request.getId());
		}
	}    

    @Test
    public void acceptProjectTest() {
        String outerId = "2020-ECNU-D-00";
        String remark = "acceptTest0402";
        Integer userId = new Integer(1);
        ApproveProjectRequest apd = projectService.acceptProject(outerId, remark, userId);
    }

	@Test
	public void rejectProjectTest() {
		String outerId = "2020-ECNU-D-06";
		String remark = "rejectTest0329";
		Integer userId = new Integer(1);
		ApproveProjectRequest apd = projectService.rejectProject(outerId, remark, userId);
	}

	@Test
	public void assignEQGTest_exception() {
		String outerId = "2020-ECNU-D-03";
		AssignRoleRequest arr = new AssignRoleRequest();
		List<Integer> userId = new ArrayList<Integer>();
		userId.add(1);
		arr.setOuterId(outerId);
		arr.setUserId(userId);
		UserContext userContext = new UserContext(1, "admin");
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			projectService.assignEPG(arr, userContext);
		});
	    assertEquals("The project should be approved by supervisor.", exception.getMessage());
	}
	
	@Test
	public void assignEQGTest() {
		String outerId = "P01";
		AssignRoleRequest arr = new AssignRoleRequest();
		List<Integer> userId = new ArrayList<Integer>();
		userId.add(20);
		arr.setOuterId(outerId);
		arr.setUserId(userId);
		UserContext userContext = new UserContext(1, "admin");
		Project project = projectService.assignEPG(arr, userContext);
		System.out.println("EPG state = "+project.getEpgAssigned());
	}

	@Test
	public void assignQATest_exception() {
		String outerId = "2020-ECNU-D-03";
		AssignRoleRequest arr = new AssignRoleRequest();
		List<Integer> userId = new ArrayList<Integer>();
		userId.add(1);
		arr.setOuterId(outerId);
		arr.setUserId(userId);
		UserContext userContext = new UserContext(1, "admin");
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			projectService.assignQA(arr, userContext);
		});
	    assertEquals("The project should be approved by supervisor.", exception.getMessage());
	}
	
	@Test
	public void assignQATest() {
		String outerId = "P01";
		AssignRoleRequest arr = new AssignRoleRequest();
		List<Integer> userId = new ArrayList<Integer>();
		userId.add(20);
		arr.setOuterId(outerId);
		arr.setUserId(userId);
		UserContext userContext = new UserContext(1, "admin");
		Project project = projectService.assignQA(arr, userContext);
		System.out.println("QA state = "+project.getQaAssigned());
	}

	@Test
	public void assignConfigTest_exception() {
		String outerId = "2020-ECNU-D-03";
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			projectService.assignConfig(outerId);
		});
	    assertEquals("The project should be approved by supervisor.", exception.getMessage());
	}
	
	@Test
	public void assignConfigTest() {
		String outerId = "P01";
		Project project = projectService.assignConfig(outerId);
		System.out.println("Config state = "+project.getConfigAssigned());
	}

	@Test
	public void setConfigInfoTest() {
		String outerId = "2020-ECNU-D-02";
		String remark = "setConfig";
		Integer userId = new Integer(1);
		Project project = projectService.setConfigInfo(outerId, remark, userId);
		System.out.println("State = "+project.getState());
	}
	
	@Test
	public void setConfigInfoTest_exception1() {
		String outerId = "2020-ECNU-D-00";
		String remark = "setConfig";
		Integer userId = new Integer(1);
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			projectService.setConfigInfo(outerId, remark, userId);
		});
	    assertEquals("The project should be approved by supervisor.", exception.getMessage());
	}
	
	@Test
	public void setConfigInfoTest_exception2() {
		String outerId = "P01";
		String remark = "setConfig";
		Integer userId = new Integer(1);
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			projectService.setConfigInfo(outerId, remark, userId);
		});
	    assertEquals("Config, EPG and QA should all be assigned first.", exception.getMessage());
	}
	
	@Test
	public void endProjectTest() {
		String outerId = "P04";
		String remark = " end project";
		Integer userId = new Integer(1);
		assertNotNull(projectService.endProject(outerId, remark, userId));
	}
	
	@Test
	public void endProjectTest_exception() {
		String outerId = "2020-ECNU-D-03";
		String remark = " end project";
		Integer userId = new Integer(1);
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			projectService.endProject(outerId, remark, userId);
		});
	    assertEquals("The project already expires, cannot be updated, please choose a new one.", exception.getMessage());
	}
	
	@Test
	public void acceptArchieveTest() {
		String outerId = "P04";
		String remark = "project archieve";
		Integer userId = new Integer(1);
		assertNotNull(projectService.acceptArchive(outerId, remark, userId));
	}
	
	@Test
	public void deliverProjectTest() {
		String outerId = "P03";
		String remark = "deliver project";
		Integer userId = new Integer(1);
		assertNotNull(projectService.projectDelivery(outerId, remark, userId));
	}
	
}
