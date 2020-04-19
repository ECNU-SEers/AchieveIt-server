package pretty.april.achieveitserver.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pretty.april.achieveitserver.activiti.ProcessManagementService;
import pretty.april.achieveitserver.dto.PageDTO;
import pretty.april.achieveitserver.entity.*;
import pretty.april.achieveitserver.mapper.*;
import pretty.april.achieveitserver.request.project.*;
import pretty.april.achieveitserver.security.UserContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ProjectService extends ServiceImpl<ProjectMapper, Project> {

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private ClientService clientService;

    @Autowired
    private SkillDictService skillService;

    @Autowired
    private ProjectSkillMapper projectSkillMapper;

    @Autowired
    private BusinessAreaDictService businessAreaService;

    @Autowired
    private ProjectBusinessAreaMapper projectBusinessAreaMapper;

    @Autowired
    private ProjectSkillService projectSkillService;

    @Autowired
    private ProjectBusinessAreaService projectBusinessAreaService;

    @Autowired
    private MilestoneService milestoneService;

    @Autowired
    private ProjectFunctionService functionService;

    @Autowired
    private MilestoneMapper milestoneMapper;

    @Autowired
    private ProjectMemberService memberService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectMemberMapper memberMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private ProcessManagementService processManagementService;
    
    @Autowired
    private StateChangeMapper stateChangeMapper;
    
    @Autowired
    private ProjectMemberService projectMemberService;
    
    @Autowired
    private ProjectMemberMapper projectMemberMapper;
    
    @Autowired
    private ProjectIdMapper projectIdMapper;
    
    @Autowired
    private RolePermissionService rolePermissionService;
    
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    
    @Autowired
    private ArchivedInfoMapper archivedInfoMapper;

    /**
     * 获得所有项目id和name
     * @return
     */
    public List<ObtainAllProjectRequest> getAllIdAndOuterIdAndName() {
    	return this.baseMapper.selectIdAndOuterIdAndName();
    }
    
    /**
     * 创建项目：项目经理录入项目信息并自动申请立项
     *
     * @param validator 新建项目校验器
     * @return 被创建的项目
     * @throws Exception
     */
    @Transactional
    public Project createProject(CreateProjectRequest validator, UserContext userContext) {
//		1.验证该项目是否存在
        boolean projectIsExist = this.checkProjectExistByOuterId(validator.getOuterId());
        if (projectIsExist) {
            throw new IllegalArgumentException("The project already exists, please choose a new one.");
        }

//		2.设置项目状态和客户表ID并创建项目
        Project project = new Project();
        BeanUtils.copyProperties(validator, project);
        project.setState("申请立项");
        project.setManagerId(userContext.getUserId());
        project.setManagerName(userContext.getUsername());
//		通过客户名称和客户ID得到客户表ID
        project.setClientId(clientService.getIdByOuterIdAndCompany(validator.getClientOuterId(), validator.getCompany()));
        project.setRemark(null);
        projectMapper.insert(project);

        Integer projectId = project.getId();
//		3.添加项目采用技术
        List<String> skillNames = validator.getSkillNames();
        ProjectSkill projectSkill = new ProjectSkill();
        projectSkill.setProjectId(projectId);
        for (String skillName : skillNames) {
            projectSkill.setSkillId(skillService.getSkillIdBySkillName(skillName));
            projectSkill.setSkillName(skillName);
            projectSkillMapper.insert(projectSkill);
        }

//		4.添加项目业务领域
//		业务领域不为空时
        if (validator.getBusinessAreaName() != null && !validator.getBusinessAreaName().equals("")) {
            ProjectBusinessArea projectBusinessArea = new ProjectBusinessArea();
            projectBusinessArea.setProjectId(projectId);
            projectBusinessArea.setBusinessAreaId(businessAreaService.getBusinessAreaIdByBusinessAreaName(validator.getBusinessAreaName()));
            projectBusinessArea.setBusinessAreaName(validator.getBusinessAreaName());
            projectBusinessAreaMapper.insert(projectBusinessArea);
        }
        
//      5.把manager和supervisor，组织配置管理员、EPGLeader和QA经理加入project_member中
        ProjectMember manager = new ProjectMember();
        manager.setProjectId(projectId);
        manager.setUserId(userContext.getUserId());
        manager.setUsername(userContext.getUsername());
        manager.setProjectName(validator.getName());
        manager.setLeaderId(validator.getSupervisorId());
        manager.setLeaderName(validator.getSupervisorName());
        projectMemberMapper.insert(manager);
        
        ProjectMember supervisor = new ProjectMember();
        supervisor.setProjectId(projectId);
        supervisor.setUserId(validator.getSupervisorId());
        supervisor.setUsername(validator.getSupervisorName());
        supervisor.setProjectName(validator.getName());
//      supervisor的leader为null
        projectMemberMapper.insert(supervisor);
        
        ProjectMember configOrganizer = new ProjectMember();
        configOrganizer.setProjectId(projectId);
        configOrganizer.setUserId(validator.getConfigOrganizerId());
        configOrganizer.setUsername(validator.getConfigOrganizerName());
        configOrganizer.setProjectName(validator.getName());
//      configOrganizer的leader为null
        projectMemberMapper.insert(configOrganizer);
        
        ProjectMember qaManager = new ProjectMember();
        qaManager.setProjectId(projectId);
        qaManager.setUserId(validator.getQaManagerId());
        qaManager.setUsername(validator.getQaManagerName());
        qaManager.setProjectName(validator.getName());
//      qaManager的leader为null
        projectMemberMapper.insert(qaManager);
        
        ProjectMember epgLeader = new ProjectMember();
        epgLeader.setProjectId(projectId);
        epgLeader.setUserId(validator.getEpgLeaderId());
        epgLeader.setUsername(validator.getEpgLeaderName());
        epgLeader.setProjectName(validator.getName());
//      epgLeader的leader为null
        projectMemberMapper.insert(epgLeader);
        
//      6.修改project_id的状态
        ProjectId pid = new ProjectId();
        pid.setProjectId(validator.getOuterId());
        pid.setIsfree(false);
        projectIdMapper.updateIsFreeByProjectId(validator.getOuterId());

//		7.启动流程实例
        Map<String, Object> map = new HashMap();
        String projectOuterId = validator.getOuterId();
        map.put("projectManager", projectOuterId + "manager");
        map.put("projectSupervisor", projectOuterId + "supervisor");
        map.put("organizationConfigurationAdministrator", projectOuterId + "organizer");
        map.put("EPGLeader", projectOuterId + "epg_leader");
        map.put("QAManager", projectOuterId + "qa_manager");
        ProcessInstance processInstanceWithBusinessKey = processManagementService.startProcessInstance("projectApproval", this.getProjectByOuterId(validator.getOuterId()).getId().toString(), map);

//		8.设置项目流程实例ID
        project.setInstanceId(processInstanceWithBusinessKey.getId());
        projectMapper.updateById(project);

//		9.查询该执行人名下所有的task
        List<Task> taskList = processManagementService.queryActivityTask("projectApproval", projectOuterId + "manager");
//		10.执行当前流程实例下的第一个task
        for (Task task : taskList) {
            if (task.getProcessInstanceId().equals(processInstanceWithBusinessKey.getId())) {
                processManagementService.handleActivityTask(task);
            }
        }
        
//      11.状态更新
        StateChange stateChange = new StateChange();
        stateChange.setProjectId(projectId);
        stateChange.setChangeDate(LocalDateTime.now());
        stateChange.setLatterState(project.getState());
        stateChange.setOperatorId(userContext.getUserId());
        stateChange.setRemark(validator.getRemark());
        stateChangeMapper.insert(stateChange);
        
        return project;
    }

    /**
     * 查询项目基本信息
     *
     * @param outerId
     * @return 项目基本信息
     */
    public RetrieveProjectRequest retrieveProject(String outerId) {
//		1.利用outerId得到project表中的所有信息
        Project project = this.getProjectByOuterId(outerId);
//		2.利用project表中的client_id得到client表中的信息
        Client client = clientService.getClientById(project.getClientId());
//		3.利用project表中的id得到project_skill表中的信息
        List<ProjectSkill> projectSkills = projectSkillService.getProjectSkillByProjectId(project.getId());
//		4.利用project表中的id得到project_business_area表中的信息
        ProjectBusinessArea projectBusinessArea = projectBusinessAreaService.getProjectBusinessAreaByProjectId(project.getId());
//		5.利用project表中的id得到milestone表中的信息
        List<Milestone> milestones = milestoneService.getProjectMilestoneByProjectId(project.getId());
//		6.利用project表中的id得到project_function表中的信息
        List<ProjectFunction> functions = functionService.getProjectFunctionByProjectId(project.getId());
//		7.查询项目对象
        RetrieveProjectRequest retrieveProject = new RetrieveProjectRequest();
        retrieveProject.setProject(project);
        retrieveProject.setProjectClient(client);
        retrieveProject.setProjectSkills(projectSkills);
        retrieveProject.setProjectBusinessArea(projectBusinessArea);
        retrieveProject.setProjectMilestones(milestones);
        retrieveProject.setProjectFunctions(functions);

        return retrieveProject;
    }
    
    /**
     * 分页查询所有项目名称中包含某关键字的项目详情
     * @param pageNo
     * @param pageSize
     * @param keyword
     * @return 所有项目名称包含该关键字的项目的详情
     */
    public PageDTO<RetrieveProjectRequest> retrieveProjectsWithNameIncluingKeywordByPage(Integer pageNo, Integer pageSize, String keyword, Integer userId) {
    	
    	Page<Project> page = this.selectProjectByNameWithKeyword(userId, keyword, new Page<Project>(pageNo, pageSize));
    	List<RetrieveProjectRequest> projectsDetails = new ArrayList<RetrieveProjectRequest>();
    	for (Project project : page.getRecords()) {
    		projectsDetails.add(this.retrieveProject(project.getOuterId()));
        }
    	return new PageDTO<RetrieveProjectRequest>(page.getCurrent(), page.getSize(), page.getTotal(), projectsDetails);
    }

    /**
     * 搜索所有项目名称中包含某关键字的项目名称和项目ID
     *
     * @param userId
     * @param keyword
     * @return 所有项目名称包含该关键字的项目名称和项目ID
     */
    public List<SearchProjectRequest> searchProjectWithNameIncludingKeyword(String keyword, Integer userId) {

//		1.利用keyword找到所有项目名称中包含该keyword的所有项目
        List<Project> projects = this.selectProjectByNameWithKeyword(userId, keyword);
//		2.得到所有项目的详细信息
        List<SearchProjectRequest> projectNameAndIds = new ArrayList<SearchProjectRequest>();
        for (Project project : projects) {
        	SearchProjectRequest result = new SearchProjectRequest();
            result.setOuterId(project.getOuterId());
            result.setName(project.getName());
            projectNameAndIds.add(result);
        }
        return projectNameAndIds;
    }

    /**
     * 展示项目列表
     *
     * @param outerId
     * @return 项目列表信息
     */
    public ShowProjectListRequest showProjectList(String outerId) {
//		1.利用outerId得到project表中的所有信息
        Project project = this.getProjectByOuterId(outerId);
//		2.利用project表中的client_id得到client表中的outer_id和company
        String clientOuterId = clientService.getById(project.getClientId()).getOuterId();
        String company = clientService.getCompanyById(project.getClientId());
        ShowProjectListRequest projectList = new ShowProjectListRequest();
        BeanUtils.copyProperties(project, projectList);
        projectList.setSupervisorRealName(userService.getById(project.getSupervisorId()).getRealName());
        projectList.setManagerRealName(userService.getById(project.getManagerId()).getRealName());
        projectList.setClientOuterId(clientOuterId);
        projectList.setCompany(company);
        projectList.setParticipantCounter(memberService.selectCountByProjectId(project.getId()));
        if (project.getQaAssigned()) {
            projectList.setQaAssigned("已分配");
        } else {
            projectList.setQaAssigned("未分配");
        }
        if (project.getEpgAssigned()) {
            projectList.setEpgAssigned("已分配");
        } else {
            projectList.setEpgAssigned("未分配");
        }
        return projectList;
    }
    
    /**
     * 查询某个QA经理参与的所有项目
     * @param userId
     * @return 某个QA经理参与的所有项目
     */
    public Page<Project> getProjectsOfQAManager(Integer userId, String keyword, Page<Project> page) {
    	return page.setRecords(this.baseMapper.selectProjectsOfQAManager(userId, keyword, page));
    }
    
    /**
     * 查询某个EPG_Leader参与的所有项目
     * @param userId
     * @return 某个EPG_Leader参与的所有项目
     */
    public Page<Project> getProjectsOfEPGLeader(Integer userId, String keyword, Page<Project> page) {
    	return page.setRecords(this.baseMapper.selectProjectsOfEPGLeader(userId, keyword, page));
    }
    
    /**
     * 查询某个用户（非QA经理 且 非EPG_Leader）参与的所有项目
     * @param userId
     * @return 某个用户参与的所有项目
     */
    public Page<Project> getProjectsOfAUser(Integer userId, String keyword, Page<Project> page) {
    	return page.setRecords(this.baseMapper.selectProjectsOfAUser(userId, keyword, page));
    }
    
    /**
     * 某个用户可查看的全部项目
     * @param pageNo
     * @param pageSize
     * @param userId 用户ID
     * @return 项目列表
     */
    public PageDTO<ShowProjectListRequest> showProjects(Integer pageNo, Integer pageSize, String keyword, Integer userId) {

    	Page<Project> page;
//    	1.利用用户ID查找用户的角色
    	List<Integer> userRoles = userRoleService.getUserRoleByUserId(userId);
//    	角色QA_MANAGER的id是3；角色EPG_LEADER的id是1
    	if (userRoles.contains(3)) {
    		page = this.getProjectsOfQAManager(userId, keyword, new Page<Project>(pageNo, pageSize));
    	} else if (userRoles.contains(1)) {
    		page = this.getProjectsOfEPGLeader(userId, keyword, new Page<Project>(pageNo, pageSize));
    	} else {
    		page = this.getProjectsOfAUser(userId, keyword, new Page<Project>(pageNo, pageSize));
    	}
    	
    	List<ShowProjectListRequest> projectLists = new ArrayList<ShowProjectListRequest>();
    	for (Project project: page.getRecords()) {
    		projectLists.add(this.showProjectList(project.getOuterId()));
    	}
    	return new PageDTO<ShowProjectListRequest>(page.getCurrent(), page.getSize(), page.getTotal(), projectLists);
    }
    
    /**
     * 更新项目：修改项目信息（不包括里程碑、项目技术和业务领域）
     * @param validator 更新项目信息校验器
     * @return 被更新的项目
     */
    @Transactional
    public Project updateProjectInfoWithoutSkillsAndBusinessAreaAndMilestone(UpdateProjectInfoRequest validator) {
//		1.利用projectId找到待修改的project，判断项目是否“结束”或“已归档”
        Project primaryProject = this.getProjectByOuterId(validator.getOuterId());
        if (primaryProject.getState().equals("结束") || primaryProject.getState().equals("已归档")) {
            throw new IllegalArgumentException("The project already expires, cannot be updated, please choose a new one.");
        }
//		2.更新project表
        Project project = new Project();
        BeanUtils.copyProperties(validator, project);
        Integer projectId = primaryProject.getId();
        project.setId(projectId);
        project.setState(primaryProject.getState());
        project.setClientId(clientService.getIdByOuterIdAndCompany(validator.getClientOuterId(), validator.getCompany()));
        project.setManagerId(primaryProject.getManagerId());
        project.setManagerName(primaryProject.getManagerName());
        project.setSupervisorId(primaryProject.getSupervisorId());
        project.setSupervisorName(primaryProject.getSupervisorName());
        projectMapper.updateById(project);

//      3.更新project_member表中的project_name
        List<ProjectMember> projectMembers = projectMemberService.selectByProjectId(projectId);
        for (ProjectMember projectMember: projectMembers) {
        	projectMember.setProjectName(validator.getName());
        	projectMemberMapper.updateProjectNameByUserIdAndProjectId(projectMember.getProjectName(), projectMember.getProjectId());
        }
        
        return project;
    }
    
    /**
     * 更新项目：修改项目信息
     * @param validator 更新项目信息校验器
     * @return 被更新的项目
     */
    @Transactional
    public Project updateProjectInfo(UpdateProjectRequest validator, Integer userId) {
//		1.利用projectId找到待修改的project，判断项目是否“结束”或“已归档”
        Project primaryProject = this.getProjectByOuterId(validator.getOuterId());
        if (primaryProject.getState().equals("结束") || primaryProject.getState().equals("已归档")) {
            throw new IllegalArgumentException("The project already expires, cannot be updated, please choose a new one.");
        }
//		2.更新project表
        Project project = new Project();
        BeanUtils.copyProperties(validator, project);
        Integer projectId = primaryProject.getId();
        project.setId(projectId);
        project.setState(primaryProject.getState());
        project.setClientId(clientService.getIdByOuterIdAndCompany(validator.getClientOuterId(), validator.getCompany()));
        project.setManagerId(primaryProject.getManagerId());
        project.setManagerName(primaryProject.getManagerName());
        project.setSupervisorId(primaryProject.getSupervisorId());
        project.setSupervisorName(primaryProject.getSupervisorName());
        projectMapper.updateById(project);

//		3.更新project_skill表：删掉旧的，添加新的
        projectSkillService.deleteProjectSkillByProjectId(projectId);

        List<String> skillNames = validator.getSkillNames();
        ProjectSkill projectSkill = new ProjectSkill();
        projectSkill.setProjectId(projectId);
        for (String skillName : skillNames) {
            projectSkill.setSkillId(skillService.getSkillIdBySkillName(skillName));
            projectSkill.setSkillName(skillName);
            projectSkillMapper.insert(projectSkill);
        }

//		4.更新project_business_area表：删掉旧的，添加新的
        projectBusinessAreaService.deleteProjectBusinessAreaByProjectId(projectId);

        if (validator.getBusinessAreaName() != null && !validator.getBusinessAreaName().equals("")) {
            ProjectBusinessArea projectBusinessArea = new ProjectBusinessArea();
            projectBusinessArea.setProjectId(projectId);
            projectBusinessArea.setBusinessAreaId(businessAreaService.getBusinessAreaIdByBusinessAreaName(validator.getBusinessAreaName()));
            projectBusinessArea.setBusinessAreaName(validator.getBusinessAreaName());
            projectBusinessAreaMapper.insert(projectBusinessArea);
        }

//		5.更新milestone表：insert一条新纪录
        if (!"".equals(validator.getMilestone()) && validator.getMilestone()!=null) {
        	Milestone milestone = new Milestone();
            milestone.setProjectId(projectId);
            milestone.setProgress(validator.getMilestone());
            milestone.setRecordDate(LocalDate.now());
            milestone.setRecorderId(userId);
            milestoneMapper.insert(milestone);
        }

//      6.更新project_member表中的project_name
        List<ProjectMember> projectMembers = projectMemberService.selectByProjectId(projectId);
        for (ProjectMember projectMember: projectMembers) {
        	projectMember.setProjectName(validator.getName());
        	projectMemberMapper.updateProjectNameByUserIdAndProjectId(projectMember.getProjectName(), projectMember.getProjectId());
        }
        
        return project;
    }

    /**
     * 因项目驳回而更新项目（只可调用一次）
     * @param validator
     * @return
     * @throws Exception
     */
    @Transactional
    public Project updateProjectInfoDuringProjectApproval(UpdateProjectRequest validator, Integer userId) {
        Project project = this.updateProjectInfo(validator, userId);
        project.setState("申请立项");
    	projectMapper.updateById(project);

//		2.查询该执行人名下所有的task
        String projectOuterId = validator.getOuterId();
        List<Task> taskList = processManagementService.queryActivityTask("projectApproval", projectOuterId + "manager");
//		3.执行当前流程实例下的第一个task
        for (Task task : taskList) {
            if (task.getProcessInstanceId().equals(this.getProjectByOuterId(validator.getOuterId()).getInstanceId())) {
                processManagementService.handleActivityTask(task);
            }
        }

        return project;
    }

    /**
     * 通过项目ID查找项目
     *
     * @param outerId 项目ID
     * @return 项目
     */
    public Project getProjectByOuterId(String outerId) {
        QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Project::getOuterId, outerId);
        return this.getOne(queryWrapper);
    }

    /**
     * 根据项目ID检查项目是否存在
     *
     * @param outerId 项目ID
     * @return true代表存在
     */
    public boolean checkProjectExistByOuterId(String outerId) {
        int rows = this.baseMapper.selectCountByOuterId(outerId);
        return rows > 0;
    }

    /**
     * 查询还用户参与的所有项目名称中包含某关键字的项目
     *
     * @param userId
     * @param keyword
     * @param page
     * @return 所有项目名称中包含某关键字的项目
     */
    public Page<Project> selectProjectByNameWithKeyword(Integer userId, String keyword, Page<Project> page) {
        return page.setRecords(this.baseMapper.selectByNameLikeKeyword(userId, keyword, page));
    }
    
    public List<Project> selectProjectByNameWithKeyword(Integer userId, String keyword) {
        return this.baseMapper.selectByNameLikeKeyword1(userId, keyword);
    }

    /**
     * 项目上级审批立项：审批通过
     *
     * @param projectInfo
     * @return 项目信息和审批结果（通过）
     * @throws Exception
     */
    @Transactional
    public ApproveProjectRequest acceptProject(String projectOuterId, String remark, Integer userId) {
    	Project primaryProject = this.getProjectByOuterId(projectOuterId);
    	if (!primaryProject.getState().equals("申请立项") && !primaryProject.getState().equals("立项驳回")) {
    		throw new IllegalArgumentException("The project is not in the state of applying for project approval.");
    	}
    	StateChange stateChange = new StateChange();
    	stateChange.setFormerState(primaryProject.getState());
    	
//    	1.更新project的状态和备注
    	primaryProject.setState("已立项");
    	primaryProject.setRemark(remark);
    	projectMapper.updateById(primaryProject);
    	
//		2.查询该执行人名下所有的task
        Map<String, Object> variable = new HashMap();
        variable.put("review_result", true);
        List<Task> taskList = processManagementService.queryActivityTask("projectApproval", projectOuterId + "supervisor");
//		3.执行当前流程实例下的第一个task
        for (Task task : taskList) {
            if (task.getProcessInstanceId().equals(primaryProject.getInstanceId())) {
                processManagementService.handleActivityTask(task, variable);
            }
        }
//      4.状态更新
        stateChange.setProjectId(primaryProject.getId());
        stateChange.setChangeDate(LocalDateTime.now());
        stateChange.setLatterState(primaryProject.getState());
        stateChange.setOperatorId(userId);
        stateChange.setRemark(remark);
        stateChangeMapper.insert(stateChange);
    	
    	ApproveProjectRequest approveProject = new ApproveProjectRequest();
    	approveProject.setProjectOuterId(projectOuterId);
    	approveProject.setReviewResult(true);
    	return approveProject;
    }

    /**
     * 项目上级审批立项：驳回申请
     *
     * @param projectInfo
     * @return 项目信息和审批结果（驳回）
     * @throws Exception
     */
    @Transactional
    public ApproveProjectRequest rejectProject(String projectOuterId, String remark, Integer userId) {
    	Project primaryProject = this.getProjectByOuterId(projectOuterId);
    	if (!primaryProject.getState().equals("申请立项") && !primaryProject.getState().equals("立项驳回")) {
    		throw new IllegalArgumentException("The project is not in the state of applying for project approval.");
    	}
    	StateChange stateChange = new StateChange();
    	stateChange.setFormerState(primaryProject.getState());
    	
//    	1.更新project的状态和备注
    	primaryProject.setState("立项驳回");
    	primaryProject.setRemark(remark);
    	projectMapper.updateById(primaryProject);
    	
//		2.查询该执行人名下所有的task
        Map<String, Object> variable = new HashMap();
        variable.put("review_result", false);
        List<Task> taskList = processManagementService.queryActivityTask("projectApproval", projectOuterId + "supervisor");
//		3.执行当前流程实例下的第一个task
        for (Task task : taskList) {
            if (task.getProcessInstanceId().equals(primaryProject.getInstanceId())) {
                processManagementService.handleActivityTask(task, variable);
            }
        }
//      4.状态更新
        stateChange.setProjectId(primaryProject.getId());
        stateChange.setChangeDate(LocalDateTime.now());
        stateChange.setLatterState(primaryProject.getState());
        stateChange.setOperatorId(userId);
        stateChange.setRemark(remark);
        stateChangeMapper.insert(stateChange);
    	
    	ApproveProjectRequest approveProject = new ApproveProjectRequest();
    	approveProject.setProjectOuterId(projectOuterId);
    	approveProject.setReviewResult(false);
    	return approveProject;
    }
    
    /**
     * 组织配置管理员为项目分配配置库
     *
     * @param request
     * @return 该项目
     */
    @Transactional
    public Project assignConfig(String outerId) {
//		1.改变project表的git_assigned
        Project project = this.getProjectByOuterId(outerId);
        if (!project.getState().equals("已立项")) {
        	throw new IllegalArgumentException("The project should be approved by supervisor.");
        }
        project.setConfigAssigned(true);
        projectMapper.updateById(project);

//		2.查询该执行人名下所有的task
        List<Task> taskList = processManagementService.queryActivityTask("projectApproval", outerId + "organizer");
//		3.执行当前流程实例下的第一个task
        for (Task task : taskList) {
            if (task.getProcessInstanceId().equals(this.getProjectByOuterId(outerId).getInstanceId())) {
                processManagementService.handleActivityTask(task);
            }
        }

        return project;
    }

    /**
     * QA经理为项目分配QA
     *
     * @param request
     * @return 该项目
     */
    @Transactional
    public Project assignQA(AssignRoleRequest request, UserContext userContext) {
//		1.改变project表的qa_assigned
        Project project = this.getProjectByOuterId(request.getOuterId());
        if (!project.getState().equals("已立项")) {
        	throw new IllegalArgumentException("The project should be approved by supervisor.");
        }
        project.setQaAssigned(true);
        projectMapper.updateById(project);
//		2.将分配的QA加入project_member表
        Integer projectId = project.getId();
        ProjectMember member = new ProjectMember();
        member.setProjectId(projectId);
        member.setProjectName(project.getName());
        member.setLeaderId(userContext.getUserId());
        member.setLeaderName(userContext.getUsername());
        for (Integer id : request.getUserId()) {
            if (!memberService.checkMemberExistByProjectIdAndUserId(projectId, id)) {
                member.setUserId(id);
                member.setUsername(userService.getById(id).getUsername());
                memberMapper.insert(member);
            }
        }
//		3.将分配的QA加入user_role表
        Integer roleId = 4; // 角色QA对应的id
        for (Integer id : request.getUserId()) {
            if (!userRoleService.checkUserRoleExistByUserIdAndRoleId(id, roleId)) {
                UserRole userRole = new UserRole();
                userRole.setUserId(id);
                userRole.setRoleId(roleId);
                userRole.setProjectId(projectId);
                userRoleMapper.insert(userRole);
            }
        }
//      将role对应的permission加入到role_permission
        Integer permissionId = new Integer(3);
        if (!rolePermissionService.checkRolePermissionExistByRoleIdAndPermissionId(roleId, permissionId)) {
        	RolePermission rolePermission = new RolePermission(roleId, permissionId);
        	rolePermissionMapper.insert(rolePermission);
        }

//		4.查询该执行人名下所有的task
        String projectOuterId = request.getOuterId();
        List<Task> taskList = processManagementService.queryActivityTask("projectApproval", projectOuterId + "qa_manager");
//		5.执行当前流程实例下的第一个task
        for (Task task : taskList) {
            if (task.getProcessInstanceId().equals(this.getProjectByOuterId(request.getOuterId()).getInstanceId())) {
                processManagementService.handleActivityTask(task);
            }
        }

        return project;
    }

    /**
     * EPG Leader为项目分配EPG
     *
     * @param request
     * @return 该项目
     */
    @Transactional
    public Project assignEPG(AssignRoleRequest request, UserContext userContext) {
//		1.改变project表的epg_assigned
        Project project = this.getProjectByOuterId(request.getOuterId());
        if (!project.getState().equals("已立项")) {
        	throw new IllegalArgumentException("The project should be approved by supervisor.");
        }
        project.setEpgAssigned(true);
        projectMapper.updateById(project);
//		2.将分配的QA加入project_member表
        Integer projectId = project.getId();
        ProjectMember member = new ProjectMember();
        member.setProjectId(projectId);
        member.setProjectName(project.getName());
        member.setLeaderId(userContext.getUserId());
        member.setLeaderName(userContext.getUsername());
        for (Integer id : request.getUserId()) {
            if (!memberService.checkMemberExistByProjectIdAndUserId(projectId, id)) {
                member.setUserId(id);
                member.setUsername(userService.getById(id).getUsername());
                memberMapper.insert(member);
            }
        }
//		3.将分配的EPG加入user_role表
        Integer roleId = 2; // 角色EPG对应的id
        for (Integer id : request.getUserId()) {
            if (!userRoleService.checkUserRoleExistByUserIdAndRoleId(id, roleId)) {
                UserRole userRole = new UserRole();
                userRole.setUserId(id);
                userRole.setRoleId(roleId);
                userRole.setProjectId(projectId);
                userRoleMapper.insert(userRole);
            }
        }
//      将role对应的permission加入到role_permission
        Integer permissionId = new Integer(3);
        if (!rolePermissionService.checkRolePermissionExistByRoleIdAndPermissionId(roleId, permissionId)) {
        	RolePermission rolePermission = new RolePermission(roleId, permissionId);
        	rolePermissionMapper.insert(rolePermission);
        }

//		4.查询该执行人名下所有的task
        String projectOuterId = request.getOuterId();
        List<Task> taskList = processManagementService.queryActivityTask("projectApproval", projectOuterId + "epg_leader");
//		5.执行当前流程实例下的第一个task
        for (Task task : taskList) {
            if (task.getProcessInstanceId().equals(this.getProjectByOuterId(request.getOuterId()).getInstanceId())) {
                processManagementService.handleActivityTask(task);
            }
        }

        return project;
    }

    /**
     * 结束项目
     *
     * @param outerId 项目ID
     * @return
     * @throws Exception
     */
    @Transactional
    public Project endProject(String outerId, String remark, Integer userId) {
        Project project = this.getProjectByOuterId(outerId);
        if (project.getState().equals("结束") || project.getState().equals("已归档")) {
            throw new IllegalArgumentException("The project already expires, cannot be updated, please choose a new one.");
        }
        StateChange stateChange = new StateChange();
        stateChange.setFormerState(project.getState());
        
        project.setState("结束");
        projectMapper.updateById(project);
        
        stateChange.setProjectId(project.getId());
        stateChange.setChangeDate(LocalDateTime.now());
        stateChange.setLatterState(project.getState());
        stateChange.setOperatorId(userId);
        stateChange.setRemark(remark);
        stateChangeMapper.insert(stateChange);
        
        ArchivedInfo archievedInfo = new ArchivedInfo();
        archievedInfo.setProjectId(project.getId());
        archivedInfoMapper.insert(archievedInfo);
        
        return project;
    }

    /**
     * 同意归档
     *
     * @param outerId 项目ID
     * @return
     */
    @Transactional
    public Project acceptArchive(String outerId, String remark, Integer userId) {
        Project project = this.getProjectByOuterId(outerId);
        StateChange stateChange = new StateChange();
        stateChange.setFormerState(project.getState());
        
        project.setState("已归档");
        projectMapper.updateById(project);
        
        stateChange.setProjectId(project.getId());
        stateChange.setChangeDate(LocalDateTime.now());
        stateChange.setLatterState(project.getState());
        stateChange.setOperatorId(userId);
        stateChange.setRemark(remark);
        stateChangeMapper.insert(stateChange);
        
        return project;
    }

    /**
     * 项目经理配置项目信息
     *
     * @param outerId 项目ID
     * @return
     */
    @Transactional
    public Project setConfigInfo(String outerId, String remark, Integer userId) {
//		1.改变project表的git_assigned
        Project project = this.getProjectByOuterId(outerId);
        if (!project.getState().equals("已立项")) {
        	throw new IllegalArgumentException("The project should be approved by supervisor.");
        }
        if (project.getConfigAssigned()==false || project.getEpgAssigned()==false || project.getQaAssigned()==false) {
        	throw new IllegalArgumentException("Config, EPG and QA should all be assigned first.");
        }
        
        StateChange stateChange = new StateChange();
        stateChange.setFormerState(project.getState());
        
        project.setState("进行中");
        projectMapper.updateById(project);

//		2.查询该执行人名下所有的task
        List<Task> taskList = processManagementService.queryActivityTask("projectApproval", outerId + "manager");
//		3.执行当前流程实例下的第一个task
        for (Task task : taskList) {
            if (task.getProcessInstanceId().equals(this.getProjectByOuterId(outerId).getInstanceId())) {
                processManagementService.handleActivityTask(task);
            }
        }
//      4.状态更新
        stateChange.setProjectId(project.getId());
        stateChange.setChangeDate(LocalDateTime.now());
        stateChange.setLatterState(project.getState());
        stateChange.setOperatorId(userId);
        stateChange.setRemark(remark);
        stateChangeMapper.insert(stateChange);

        return project;
    }
    
    /**
     * 项目经理交付项目
     * @param outerId
     * @return
     */
    @Transactional
    public Project projectDelivery(String outerId, String remark, Integer userId) {
    	Project project = this.getProjectByOuterId(outerId);
    	StateChange stateChange = new StateChange();
        stateChange.setFormerState(project.getState());
        
        project.setState("已交付");
        projectMapper.updateById(project);
        
        stateChange.setProjectId(project.getId());
        stateChange.setChangeDate(LocalDateTime.now());
        stateChange.setLatterState(project.getState());
        stateChange.setOperatorId(userId);
        stateChange.setRemark(remark);
        stateChangeMapper.insert(stateChange);
        
        return project;
    }
}
