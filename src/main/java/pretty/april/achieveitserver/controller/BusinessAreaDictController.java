package pretty.april.achieveitserver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pretty.april.achieveitserver.dto.Response;
import pretty.april.achieveitserver.entity.BusinessAreaDict;
import pretty.april.achieveitserver.service.BusinessAreaDictService;
import pretty.april.achieveitserver.utils.ResponseUtils;

@RestController
@RequestMapping("/api/businessAreaDict")
public class BusinessAreaDictController {

	@Autowired
	private BusinessAreaDictService businessAreaDictService;
	
	@GetMapping("/allBusinessAreas")
	public Response<List<BusinessAreaDict>> getAllBusinessAreas() {
		return ResponseUtils.successResponse(businessAreaDictService.getAllBusinessAreas());
	}
}
