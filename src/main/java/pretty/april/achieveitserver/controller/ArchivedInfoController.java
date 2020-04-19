package pretty.april.achieveitserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pretty.april.achieveitserver.dto.Response;
import pretty.april.achieveitserver.entity.ArchivedInfo;
import pretty.april.achieveitserver.service.ArchivedInfoService;
import pretty.april.achieveitserver.utils.ResponseUtils;

@RestController
@RequestMapping("/api/archived/info")
public class ArchivedInfoController {

	@Autowired
	private ArchivedInfoService archivedInfoService;
	
	@GetMapping("/get")
	public Response<ArchivedInfo> getArchivedInfo(@RequestParam(value="id") Integer id) {
		return ResponseUtils.successResponse(archivedInfoService.getArchivedInfo(id));
	}
	
	@PutMapping("/update")
	public Response<Integer> updateArchivedInfo(@RequestBody ArchivedInfo updateArchivedInforequest) {
		return ResponseUtils.successResponse(archivedInfoService.updateArchivedInfo(updateArchivedInforequest));
	}
}
