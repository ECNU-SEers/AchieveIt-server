package pretty.april.achieveitserver.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import pretty.april.achieveitserver.request.project.RetrieveProjectRequest;
import pretty.april.achieveitserver.request.project.SearchProjectRequest;
import pretty.april.achieveitserver.request.project.ShowProjectListRequest;
import pretty.april.achieveitserver.request.project.UpdateProjectInfoRequest;
import pretty.april.achieveitserver.request.project.UpdateProjectRequest;
import pretty.april.achieveitserver.service.ProjectService;
import pretty.april.achieveitserver.utils.ResponseUtils;

@RestController
@RequestMapping("/api/project")
public class ProjectController {
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private ExternalSystemController externalSystemController;
	
	/**
	 * 利用关键字搜索（010101）
	 * @param userId
	 * @param keyword
	 * @return 包含关键字的项目ID和项目名称
	 */
	@GetMapping("/search")
	public Response<List<SearchProjectRequest>> searchProjectUsingKeyword(@RequestParam(value="userId") Integer userId, 
																		@RequestParam(value="keyword") String keyword) {
		return ResponseUtils.successResponse(projectService.searchProjectWithNameIncludingKeyword(userId, keyword));
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
			 																					@RequestParam(value="userId") Integer userId,
			 																					@RequestParam(value="keyword") String keyword) {
		return ResponseUtils.successResponse(projectService.retrieveProjectsWithNameIncluingKeywordByPage(pageNo, pageSize, userId, keyword));
	}
	
	/**
	 * 项目经理新建项目（自动立项）（010102）
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/create")
	public Response<?> createProject(@RequestBody CreateProjectRequest request) throws Exception {
		Project project = projectService.createProject(request);
		externalSystemController.sendmail("申请立项", project.getSupervisorId());
		return ResponseUtils.successResponse();	
	}
	
	/**
	 * 展示项目列表 （01010301）
	 * @param pageNo
	 * @param pageSize
	 * @param userId 用户ID
	 * @return
	 */
	@GetMapping("show/all/projects")
	public Response<PageDTO<ShowProjectListRequest>> showProjects(@RequestParam(value="pageNo") Integer pageNo,
													 @RequestParam(value="pageSize") Integer pageSize,
													 @RequestParam(value="userId") Integer userId) {
		
		return ResponseUtils.successResponse(projectService.showProjects(pageNo, pageSize, userId));
	}
	
	/**
	 * 结束项目（01010305）
	 * @param outerId 项目ID
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/end")
	public Response<?> endProject(@RequestParam(value="outerId") String outerId) throws Exception {
		projectService.endProject(outerId);
		return ResponseUtils.successResponse();	
	}
	
	/**
	 * 审批归档（01010306）
	 * @param outerId 项目ID
	 * @return
	 */
	@PutMapping("/approve/archive")
	public Response<?> approveArchive(@RequestParam(value="outerId") String outerId) {
		projectService.acceptArchive(outerId);
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
		Project project = projectService.assignQA(request);
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
		Project project = projectService.assignEPG(request);
		externalSystemController.sendmail("已分配EPG", project.getManagerId());
		return ResponseUtils.successResponse();	
	}
	
	/**
	 * 项目上级审批立项：同意（01010308）
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@PutMapping("/accept")
	public Response<?> acceptProject(@RequestParam(value="projectOuterId") String projectOuterId, @RequestParam(value="remark") String remark) throws Exception {
		ApproveProjectRequest project = projectService.acceptProject(projectOuterId, remark);
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
	 * @throws Exception 
	 */
	@PutMapping("/reject")
	public Response<?> rejectProject(@RequestParam(value="projectOuterId") String projectOuterId, @RequestParam(value="remark") String remark) throws Exception {
		projectService.rejectProject(projectOuterId, remark);
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
	 * @throws Exception
	 */
	@PutMapping("/update/less")
	public Response<?> updateProjectInfoWithoutSkillsAndBusinessAreaAndMilestone(@RequestBody UpdateProjectInfoRequest request) throws Exception {
		projectService.updateProjectInfoWithoutSkillsAndBusinessAreaAndMilestone(request);
		return ResponseUtils.successResponse();
	}
	
	/**
	 * 更新项目信息（010202）
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/update")
	public Response<?> updateProject(@RequestBody UpdateProjectRequest request) throws Exception {
		projectService.updateProjectInfo(request);
		return ResponseUtils.successResponse();
	}
	
	/**
	 * 因立项驳回而更新项目信息（注意：该接口只可执行一次，如果需要反复修改，调用上一个更新接口，最后一次用这个）
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/update/reject")
	public Response<?> updateProjectDuringProjectApproval(@RequestBody UpdateProjectRequest request) throws Exception {
		projectService.updateProjectInfoDuringProjectApproval(request);
		return ResponseUtils.successResponse();
	}

	/**
	 * 项目经理配置项目信息
	 * @param outerId
	 * @return
	 */
	@PutMapping("/set/config")
	public Response<?> setConfigInfo(@RequestParam(value="outerId") String outerId) {
		projectService.setConfigInfo(outerId);
		return ResponseUtils.successResponse();
	}
	
	/**
	 * 项目经理交付项目
	 * @param outerId
	 * @return
	 */
	@PutMapping("/deliver")
	public Response<?> projectDelivery(@RequestParam(value="outerId") String outerId) {
		projectService.projectDelivery(outerId);
		return ResponseUtils.successResponse();
	}
}
