package pretty.april.achieveitserver.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pretty.april.achieveitserver.activiti.ProcessManagementService;
import pretty.april.achieveitserver.entity.*;
import pretty.april.achieveitserver.mapper.*;
import pretty.april.achieveitserver.request.project.*;

import java.time.Clock;
import java.time.LocalDate;
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

    /**
     * 创建项目：项目经理录入项目信息并自动申请立项
     *
     * @param validator 新建项目校验器
     * @return 被创建的项目
     * @throws Exception
     */
    @Transactional
    public Project createProject(CreateProjectRequest validator) throws Exception {
//		1.验证该项目是否存在
        boolean projectIsExist = this.checkProjectExistByOuterId(validator.getOuterId());
        if (projectIsExist) {
            throw new Exception("The project already exists, please choose a new one.");
        }

//		2.设置项目状态和客户表ID并创建项目
        Project project = new Project();
        BeanUtil.copyProperties(validator, project);
        project.setState("申请立项");
//		通过客户名称和客户ID得到客户表ID
        project.setClientId(clientService.getIdByOuterIdAndCompany(validator.getClientOuterId(), validator.getCompany()));
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

//		5.启动流程实例
        Map<String, Object> map = new HashMap();
        String projectOuterId = validator.getOuterId();
        map.put("projectManager", projectOuterId + "manager");
        map.put("projectSupervisor", projectOuterId + "supervisor");
        map.put("organizationConfigurationAdministrator", projectOuterId + "organizer");
        map.put("EPGLeader", projectOuterId + "epg_leader");
        map.put("QAManager", projectOuterId + "qa_manager");
        ProcessInstance processInstanceWithBusinessKey = processManagementService.startProcessInstance("projectApproval", this.getProjectByOuterId(validator.getOuterId()).getId().toString(), map);

//		6.设置项目流程实例ID
        project.setInstanceId(processInstanceWithBusinessKey.getId());
        projectMapper.updateById(project);

//		7.查询该执行人名下所有的task
        List<Task> taskList = processManagementService.queryActivityTask("projectApproval", projectOuterId + "manager");
//		8.执行当前流程实例下的第一个task
        for (Task task : taskList) {
            if (task.getProcessInstanceId().equals(processInstanceWithBusinessKey.getId())) {
                processManagementService.handleActivityTask(task);
            }
        }

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
     * 查询所有项目名称中包含某关键字的项目详情
     *
     * @param keyword
     * @return 所有项目名称包含该关键字的项目的详情
     */
    public List<RetrieveProjectRequest> retrieveProjectWithNameIncludingKeyword(String keyword) {
//		1.利用keyword找到所有项目名称中包含该keyword的所有项目
        List<Project> projects = this.selectProjectByNameWithKeyword(keyword);
//		2.得到所有项目的详细信息
        List<RetrieveProjectRequest> projectsDetails = new ArrayList<RetrieveProjectRequest>();
        for (Project project : projects) {
            projectsDetails.add(this.retrieveProject(project.getOuterId()));
        }
        return projectsDetails;
    }

    /**
     * 搜索所有项目名称中包含某关键字的项目名称和项目ID
     *
     * @param keyword
     * @return 所有项目名称包含该关键字的项目名称和项目ID
     */
    public List<SearchProjectRequest> searchProjectWithNameIncludingKeyword(String keyword) {
//		1.利用keyword找到所有项目名称中包含该keyword的所有项目
        List<Project> projects = this.selectProjectByNameWithKeyword(keyword);
//		2.得到所有项目的详细信息
        List<SearchProjectRequest> projectNameAndIds = new ArrayList<SearchProjectRequest>();
        SearchProjectRequest result = new SearchProjectRequest();
        for (Project project : projects) {
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
//		2.利用project表中的client_id得到client表中的company
        String company = clientService.getCompanyById(project.getClientId());
        ShowProjectListRequest projectList = new ShowProjectListRequest();
        BeanUtil.copyProperties(project, projectList);
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

    @Transactional
    /**
     * 更新项目：修改项目信息
     * @param validator 更新项目信息校验器
     * @return 被更新的项目
     */
    public Project updateProjectInfo(UpdateProjectRequest validator) throws Exception {
//		1.利用projectId找到待修改的project，判断项目是否“结束”或“已归档”
        Project primaryProject = this.getProjectByOuterId(validator.getOuterId());
        if (primaryProject.getState().equals("结束") || primaryProject.getState().equals("已归档")) {
            throw new Exception("The project already expires, cannot be updated, please choose a new one.");
        }
//		2.更新project表
        Project project = new Project();
        BeanUtil.copyProperties(validator, project);
        Integer projectId = primaryProject.getId();
        project.setId(projectId);
        project.setState(primaryProject.getState());
        project.setClientId(clientService.getIdByOuterIdAndCompany(validator.getClientOuterId(), validator.getCompany()));
        project.setManagerId(primaryProject.getManagerId());
        project.setManagerName(primaryProject.getManagerName());
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
        Milestone milestone = new Milestone();
        milestone.setProjectId(projectId);
        milestone.setProgress(validator.getMilestone());
        milestone.setRecordDate(LocalDate.now(Clock.systemUTC()));
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getByUsername(username);
        milestone.setRecorderId(user.getId());
        milestoneMapper.insert(milestone);

        return project;
    }

    public Project updateProjectInfoDuringProjectApproval(UpdateProjectRequest validator) throws Exception {
        Project project = this.updateProjectInfo(validator);

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
     * 查询所有项目名称中包含某关键字的项目
     *
     * @param keyword
     * @return 所有项目名称中包含某关键字的项目
     */
    public List<Project> selectProjectByNameWithKeyword(String keyword) {
        return this.baseMapper.selectByNameLikeKeyword(keyword);
    }

    /**
     * 项目上级审批立项：审批通过
     *
     * @param projectInfo
     * @return 项目信息和审批结果（通过）
     * @throws Exception
     */
    public ApproveProjectRequest acceptProject(RetrieveProjectRequest projectInfo) throws Exception {
        if (!projectInfo.getProject().getState().equals("申请立项") && !projectInfo.getProject().getState().equals("立项驳回")) {
            throw new Exception("The project is not in the state of applying for project approval.");
        }
        ApproveProjectRequest approveProject = new ApproveProjectRequest();
        approveProject.setProjectInfo(projectInfo);
        approveProject.setReviewResult(true);
        Project project = new Project();
        BeanUtil.copyProperties(projectInfo.getProject(), project);
        project.setState("已立项");
        projectMapper.updateById(project);

//		2.查询该执行人名下所有的task
        Map<String, Object> variable = new HashMap();
        String projectOuterId = projectInfo.getProject().getOuterId();
        variable.put("review_result", true);
        List<Task> taskList = processManagementService.queryActivityTask("projectApproval", projectOuterId + "supervisor");
//		3.执行当前流程实例下的第一个task
        for (Task task : taskList) {
            if (task.getProcessInstanceId().equals(projectInfo.getProject().getInstanceId())) {
                processManagementService.handleActivityTask(task, variable);
            }
        }

        return approveProject;
    }

    /**
     * 项目上级审批立项：驳回申请
     *
     * @param projectInfo
     * @return 项目信息和审批结果（驳回）
     * @throws Exception
     */
    public ApproveProjectRequest rejectProject(RetrieveProjectRequest projectInfo) throws Exception {
        if (!projectInfo.getProject().getState().equals("申请立项") && !projectInfo.getProject().getState().equals("立项驳回")) {
            throw new Exception("The project is not in the state of applying for project approval.");
        }
        ApproveProjectRequest approveProject = new ApproveProjectRequest();
        approveProject.setProjectInfo(projectInfo);
        approveProject.setReviewResult(false);
        Project project = new Project();
        BeanUtil.copyProperties(projectInfo.getProject(), project);
        project.setState("立项驳回");
        projectMapper.updateById(project);

//		2.查询该执行人名下所有的task
        Map<String, Object> variable = new HashMap();
        String projectOuterId = projectInfo.getProject().getOuterId();
        variable.put("review_result", false);
        List<Task> taskList = processManagementService.queryActivityTask("projectApproval", projectOuterId + "supervisor");
//		3.执行当前流程实例下的第一个task
        for (Task task : taskList) {
            if (task.getProcessInstanceId().equals(projectInfo.getProject().getInstanceId())) {
                processManagementService.handleActivityTask(task, variable);
            }
        }

        return approveProject;
    }

    /**
     * 组织配置管理员为项目分配配置库
     *
     * @param request
     * @return 该项目
     */
    public Project assignConfig(String outerId) {
//		1.改变project表的git_assigned
        Project project = this.getProjectByOuterId(outerId);
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
    public Project assignQA(AssignRoleRequest request) {
//		1.改变project表的qa_assigned
        Project project = this.getProjectByOuterId(request.getOuterId());
        project.setQaAssigned(true);
        projectMapper.updateById(project);
//		2.将分配的QA加入project_member表
        Integer projectId = project.getId();
        ProjectMember member = new ProjectMember();
        member.setProjectId(projectId);
        member.setProjectName(project.getName());
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
                userRoleMapper.insert(userRole);
            }
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
    public Project assignEPG(AssignRoleRequest request) {
//		1.改变project表的epg_assigned
        Project project = this.getProjectByOuterId(request.getOuterId());
        project.setEpgAssigned(true);
        projectMapper.updateById(project);
//		2.将分配的QA加入project_member表
        Integer projectId = project.getId();
        ProjectMember member = new ProjectMember();
        member.setProjectId(projectId);
        member.setProjectName(project.getName());
        for (Integer id : request.getUserId()) {
            if (!memberService.checkMemberExistByProjectIdAndUserId(projectId, id)) {
                member.setUserId(id);
                member.setUsername(userService.getById(id).getUsername());
                memberMapper.insert(member);
            }
        }
//		3.将分配的EPG加入user_role表
        Integer roleId = 3; // 角色EPG对应的id
        for (Integer id : request.getUserId()) {
            if (!userRoleService.checkUserRoleExistByUserIdAndRoleId(id, roleId)) {
                UserRole userRole = new UserRole();
                userRole.setUserId(id);
                userRole.setRoleId(roleId);
                userRoleMapper.insert(userRole);
            }
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
    public Project endProject(String outerId) throws Exception {
        Project project = this.getProjectByOuterId(outerId);
        if (project.getState().equals("结束") || project.getState().equals("已归档")) {
            throw new Exception("The project already expires, cannot be updated, please choose a new one.");
        }
        project.setState("结束");
        projectMapper.updateById(project);
        return project;
    }

    /**
     * 同意归档
     *
     * @param outerId 项目ID
     * @return
     */
    public Project acceptArchive(String outerId) {
        Project project = this.getProjectByOuterId(outerId);
        project.setState("已归档");
        projectMapper.updateById(project);
        return project;
    }

    /**
     * 项目经理配置项目信息
     *
     * @param outerId 项目ID
     * @return
     */
    public Project setConfigInfo(String outerId) {
//		1.改变project表的git_assigned
        Project project = this.getProjectByOuterId(outerId);
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

        return project;
    }
}
