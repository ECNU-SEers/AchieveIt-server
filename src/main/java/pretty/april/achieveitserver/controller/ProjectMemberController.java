package pretty.april.achieveitserver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pretty.april.achieveitserver.dto.Response;
import pretty.april.achieveitserver.request.member.RetrieveBasicMemberInfoRequest;
import pretty.april.achieveitserver.service.ProjectMemberService;
import pretty.april.achieveitserver.utils.ResponseUtils;

@RestController
@RequestMapping("/api/project/members")
public class ProjectMemberController {

	@Autowired
	private ProjectMemberService projectMemberService;
	
	@GetMapping("/with/device/permissions")
    public Response<List<RetrieveBasicMemberInfoRequest>> getProjectMembersWithDeviceQueryAndManagementPermission(@RequestParam(value="projectId") Integer projectId) {
    	return ResponseUtils.successResponse(projectMemberService.selectProjectMembersWithDeviceQueryAndManagementPermission(projectId));
    }
}
