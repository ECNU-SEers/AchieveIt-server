package pretty.april.achieveitserver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pretty.april.achieveitserver.dto.Response;
import pretty.april.achieveitserver.request.project.AssignRoleRequest;
import pretty.april.achieveitserver.request.project.CreateProjectRequest;
import pretty.april.achieveitserver.request.project.RetrieveProjectRequest;
import pretty.april.achieveitserver.request.project.SearchProjectRequest;
import pretty.april.achieveitserver.request.project.ShowProjectListRequest;
import pretty.april.achieveitserver.request.project.UpdateProjectRequest;
import pretty.april.achieveitserver.service.ProjectService;
import pretty.april.achieveitserver.utils.ResponseUtils;

@RestController
@RequestMapping("/api/project")
public class ProjectController {
	
	@Autowired
	private ProjectService projectService;
	
	/**
	 * 利用关键字搜索（010101）
	 * @param keyword
	 * @return 包含关键字的项目ID和项目名称
	 */
	@GetMapping("/searchProject")
	public List<SearchProjectRequest> searchProjectUsingKeyword(@RequestParam(value="keyword") String keyword) {
		return projectService.searchProjectWithNameIncludingKeyword(keyword);
	}
	
	/**
	 * 项目经理新建项目（自动立项）（010102）
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/createProject")
	public Response<?> createProject(@RequestBody CreateProjectRequest request) throws Exception {
		projectService.createProject(request);
		return ResponseUtils.successResponse();	
	}
	
	/**
	 * 列表展示（01010301）
	 * @param outerId 项目ID
	 * @return 项目列表
	 */
	@GetMapping("/showList")
	public ShowProjectListRequest showProjectList(@RequestParam(value="outerId") String outerId) {
		return projectService.showProjectList(outerId);
	}
	
	/**
	 * 结束项目（01010305）
	 * @param outerId 项目ID
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/endProject")
	public Response<?> endProject(@RequestParam(value="outerId") String outerId) throws Exception {
		projectService.endProject(outerId);
		return ResponseUtils.successResponse();	
	}
	
	/**
	 * 审批归档
	 * @param outerId 项目ID
	 * @return
	 */
//	@PutMapping("/approveArchive")
//	public Response<?> approveArchive(@RequestParam(value="outerId") String outerId) {
//		projectService.acceptArchive(outerId);
//		return ResponseUtils.successResponse();	
//	}
	
	/**
	 * 分配配置库（01010307）
	 * @param request
	 * @return
	 */
	@PutMapping("/assignConfig")
	public Response<?> assignConfig(@RequestParam(value="outerId") String outerId) {
		projectService.assignConfig(outerId);
		return ResponseUtils.successResponse();	
	}
	
	/**
	 * 分配QA（01010307）
	 * @param request
	 * @return
	 */
	@PutMapping("/assignQA")
	public Response<?> assignQA(@RequestBody AssignRoleRequest request) {
		projectService.assignQA(request);
		return ResponseUtils.successResponse();	
	}
	
	/**
	 * 分配EPG（01010307）
	 * @param request
	 * @return
	 */
	@PutMapping("/assignEPG")
	public Response<?> assignEPG(@RequestBody AssignRoleRequest request) {
		projectService.assignEPG(request);
		return ResponseUtils.successResponse();	
	}
	
	/**
	 * 项目上级审批立项：同意（01010308）
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@PutMapping("/acceptProject")
	public Response<?> acceptProject(@RequestBody RetrieveProjectRequest request) throws Exception {
		projectService.acceptProject(request);
		return ResponseUtils.successResponse();	
	}
	
	/**
	 * 项目上级审批立项：不同意（01010308）
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@PutMapping("/rejectProject")
	public Response<?> rejectProject(@RequestBody RetrieveProjectRequest request) throws Exception {
		projectService.rejectProject(request);
		return ResponseUtils.successResponse();	
	}
	
	/**
	 * 查询项目信息（010201）
	 * @param outerId 项目ID
	 * @return 项目信息
	 */
	@GetMapping("/retrieveProjectInfo")
	public RetrieveProjectRequest retrieveProjectInfoByOuterId(@RequestParam(value="outerId") String outerId) {
		return projectService.retrieveProject(outerId);
	}
	
	/**
	 * 更新项目信息（010202）
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/updateProjectInfo")
	public Response<?> updateProject(@RequestBody UpdateProjectRequest request) throws Exception {
		projectService.updateProjectInfo(request);
		return ResponseUtils.successResponse();
	}
	
	/**
	 * 因立项驳回而更新项目信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/updateProjectInfoDuringProjectApproval")
	public Response<?> updateProjectDuringProjectApproval(@RequestBody UpdateProjectRequest request) throws Exception {
		projectService.updateProjectInfoDuringProjectApproval(request);
		return ResponseUtils.successResponse();
	}

	/**
	 * 项目经理配置项目信息
	 * @param outerId
	 * @return
	 */
	@PutMapping("/setConfigInfo")
	public Response<?> setConfigInfo(@RequestParam(value="outerId") String outerId) {
		projectService.setConfigInfo(outerId);
		return ResponseUtils.successResponse();
	}
}
