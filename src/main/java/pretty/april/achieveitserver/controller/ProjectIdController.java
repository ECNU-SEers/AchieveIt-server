package pretty.april.achieveitserver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pretty.april.achieveitserver.dto.Response;
import pretty.april.achieveitserver.entity.ProjectId;
import pretty.april.achieveitserver.service.ProjectIdService;
import pretty.april.achieveitserver.utils.ResponseUtils;

@RestController
@RequestMapping("/api/project/id")
public class ProjectIdController {

	@Autowired
	private ProjectIdService projectIdService;
	
	/**
	 * 所有项目ID
	 * @return
	 */
	@GetMapping("/all")
	public Response<List<ProjectId>> getAllProjectIds() {
		return ResponseUtils.successResponse(projectIdService.getAllProjectIds());
	}
}
