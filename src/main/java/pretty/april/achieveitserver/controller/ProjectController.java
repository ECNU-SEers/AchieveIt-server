package pretty.april.achieveitserver.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pretty.april.achieveitserver.dto.PageDTO;
import pretty.april.achieveitserver.dto.Response;
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
import pretty.april.achieveitserver.utils.ResponseUtils;

@RestController
@RequestMapping("/api/project")
public class ProjectController {
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private ExternalSystemController externalSystemController;
	
	@GetMapping("/all")
	public Response<List<ObtainAllProjectRequest>> getAllIdAndOuterIdAndName() {
		return ResponseUtils.successResponse(projectService.getAllIdAndOuterIdAndName());
	}
	
	/**
	 * 利用关键字搜索（010101）
	 * @param keyword
	 * @return 包含关键字的项目ID和项目名称
	 */
	@GetMapping("/search")
	public Response<List<SearchProjectRequest>> searchProjectUsingKeyword(@RequestParam(value="keyword") String keyword) {
    	UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userContext.getUserId();
		return ResponseUtils.successResponse(projectService.searchProjectWithNameIncludingKeyword(keyword, userId));
	}
	
	/**
	 * 搜索过程中选择某个项目进行列表展示（010101）
	 * @param outerId 项目ID
	 * @return 项目列表
	 */
	@GetMapping("/show/list")
	public Response<ShowProjectListRequest> showProjectList(@RequestParam(value="outerId") String outerId) {
		return ResponseUtils.successResponse(projectService.showProjectList(outerId));
	}
	
	/**
	 * 展示某个用户参与的所有项目名称中包含某关键字的项目基本信息
	 * @param pageNo
	 * @param pageSize
	 * @param keyword
	 * @return 某个用户参与的所有项目名称中包含某关键字的项目基本信息
	 */
	@GetMapping("/retrieve/all/keyword")
	public Response<PageDTO<RetrieveProjectRequest>> retrieveProjectInfoWithNameIncluingKeyword(@RequestParam(value="pageNo") Integer pageNo,
			 																					@RequestParam(value="pageSize") Integer pageSize,
			 																					@RequestParam(value="keyword") String keyword) {
    	UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userContext.getUserId();
		return ResponseUtils.successResponse(projectService.retrieveProjectsWithNameIncluingKeywordByPage(pageNo, pageSize, keyword, userId));
	}
	
	/**
	 * 项目经理新建项目（自动立项）（010102）
	 * @param request
	 * @return
	 */
	@PostMapping("/create")
	public Response<?> createProject(@RequestBody CreateProjectRequest request) {
		UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Project project = projectService.createProject(request, userContext);
		externalSystemController.sendmail("申请立项", project.getSupervisorId());
		return ResponseUtils.successResponse();	
	}
	
	/**
	 * 展示项目列表，可利用关键字搜索项目名称 （01010301）
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@GetMapping("show/all/projects")
	public Response<PageDTO<ShowProjectListRequest>> showProjects(@RequestParam(value="pageNo") Integer pageNo,
													 @RequestParam(value="pageSize") Integer pageSize,
													 @RequestParam(value="keyword") String keyword) {
    	UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userContext.getUserId();
		return ResponseUtils.successResponse(projectService.showProjects(pageNo, pageSize, keyword, userId));
	}
	
	/**
	 * 结束项目（01010305）
	 * @param outerId 项目ID
	 * @return
	 */
	@PutMapping("/end")
	public Response<?> endProject(@RequestParam(value="outerId") String outerId, @RequestParam(value="remark") String remark) {
		UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userContext.getUserId();
		projectService.endProject(outerId, remark, userId);
		return ResponseUtils.successResponse();	
	}
	
	/**
	 * 审批归档（01010306）
	 * @param outerId 项目ID
	 * @return
	 */
	@PutMapping("/approve/archive")
	public Response<?> approveArchive(@RequestParam(value="outerId") String outerId, @RequestParam(value="remark") String remark) {
		UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userContext.getUserId();
		projectService.acceptArchive(outerId, remark, userId);
		return ResponseUtils.successResponse();	
	}
	
	/**
	 * 分配配置库（01010307）
	 * @param request
	 * @return
	 */
	@PutMapping("/assign/config")
	public Response<?> assignConfig(@RequestParam(value="outerId") String outerId) {
		Project project = projectService.assignConfig(outerId);
		List<String> roles = new ArrayList<String>();
		externalSystemController.git(project.getOuterId(), 1, roles, LocalDateTime.now(), LocalDateTime.now().plusDays(100), "repository");
		externalSystemController.sendmail("已建立配置库", project.getManagerId());
		return ResponseUtils.successResponse();	
	}
	
	/**
	 * 分配QA（01010307）
	 * @param request
	 * @return
	 */
	@PutMapping("/assign/qa")
	public Response<?> assignQA(@RequestBody AssignRoleRequest request) {
		UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Project project = projectService.assignQA(request, userContext);
		externalSystemController.sendmail("已分配QA", project.getManagerId());
		return ResponseUtils.successResponse();	
	}
	
	/**
	 * 分配EPG（01010307）
	 * @param request
	 * @return
	 */
	@PutMapping("/assign/epg")
	public Response<?> assignEPG(@RequestBody AssignRoleRequest request) {
		UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Project project = projectService.assignEPG(request, userContext);
		externalSystemController.sendmail("已分配EPG", project.getManagerId());
		return ResponseUtils.successResponse();	
	}
	
	/**
	 * 项目上级审批立项：同意（01010308）
	 * @param request
	 * @return
	 */
	@PutMapping("/accept")
	public Response<?> acceptProject(@RequestParam(value="projectOuterId") String projectOuterId, @RequestParam(value="remark") String remark) {
		UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userContext.getUserId();
		projectService.acceptProject(projectOuterId, remark, userId);
		externalSystemController.sendmail("立项成功", 1);
		externalSystemController.sendmail("立项成功", 2);
		externalSystemController.sendmail("立项成功", 3);
		List<String> roles = new ArrayList<String>();
		externalSystemController.git(projectOuterId, 1, roles, LocalDateTime.now(), LocalDateTime.now().plusDays(100), "repository");
		externalSystemController.mail(projectOuterId, 1, roles, LocalDateTime.now(), LocalDateTime.now().plusDays(100), "mailList");
		externalSystemController.file(projectOuterId, 1, roles, LocalDateTime.now(), LocalDateTime.now().plusDays(100), "fileServerContent");
		return ResponseUtils.successResponse();	
	}
	
	/**
	 * 项目上级审批立项：不同意（01010308）
	 * @param request
	 * @return
	 */
	@PutMapping("/reject")
	public Response<?> rejectProject(@RequestParam(value="projectOuterId") String projectOuterId, @RequestParam(value="remark") String remark) {
		UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userContext.getUserId();
		projectService.rejectProject(projectOuterId, remark, userId);
		return ResponseUtils.successResponse();	
	}
	
	/**
	 * 查询项目信息（01010303 & 010201）
	 * @param outerId 项目ID
	 * @return 项目信息
	 */
	@GetMapping("/retrieve")
	public Response<RetrieveProjectRequest> retrieveProjectInfoByOuterId(@RequestParam(value="outerId") String outerId) {
		return ResponseUtils.successResponse(projectService.retrieveProject(outerId));
	}
	
	/**
	 * 更新项目信息（01010304）（修改内容不包括里程碑、技术和业务领域）
	 * @param request
	 * @return
	 */
	@PutMapping("/update/less")
	public Response<?> updateProjectInfoWithoutSkillsAndBusinessAreaAndMilestone(@RequestBody UpdateProjectInfoRequest request) {
		projectService.updateProjectInfoWithoutSkillsAndBusinessAreaAndMilestone(request);
		return ResponseUtils.successResponse();
	}
	
	/**
	 * 更新项目信息（010202）
	 * @param request
	 * @return
	 */
	@PutMapping("/update")
	public Response<?> updateProject(@RequestBody UpdateProjectRequest request) {
        UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Integer userId = userContext.getUserId();
		projectService.updateProjectInfo(request, userId);
		return ResponseUtils.successResponse();
	}
	
	/**
	 * 因立项驳回而更新项目信息（注意：该接口只可执行一次，如果需要反复修改，调用上一个更新接口，最后一次用这个）
	 * @param request
	 * @return
	 */
	@PutMapping("/update/reject")
	public Response<?> updateProjectDuringProjectApproval(@RequestBody UpdateProjectRequest request) {
        UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Integer userId = userContext.getUserId();
		projectService.updateProjectInfoDuringProjectApproval(request, userId);
		return ResponseUtils.successResponse();
	}

	/**
	 * 项目经理配置项目信息
	 * @param outerId
	 * @return
	 */
	@PutMapping("/set/config")
	public Response<?> setConfigInfo(@RequestParam(value="outerId") String outerId, @RequestParam(value="remark") String remark) {
		UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userContext.getUserId();
		projectService.setConfigInfo(outerId, remark, userId);
		return ResponseUtils.successResponse();
	}
	
	/**
	 * 项目经理交付项目
	 * @param outerId
	 * @return
	 */
	@PutMapping("/deliver")
	public Response<?> projectDelivery(@RequestParam(value="outerId") String outerId, @RequestParam(value="remark") String remark) {
		UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userContext.getUserId();
		projectService.projectDelivery(outerId, remark, userId);
		return ResponseUtils.successResponse();
	}
}
