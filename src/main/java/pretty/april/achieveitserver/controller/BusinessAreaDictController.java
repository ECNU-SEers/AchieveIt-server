package pretty.april.achieveitserver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pretty.april.achieveitserver.dto.Response;
import pretty.april.achieveitserver.entity.BusinessAreaDict;
import pretty.april.achieveitserver.service.BusinessAreaDictService;
import pretty.april.achieveitserver.utils.ResponseUtils;

@RestController
@RequestMapping("/api/business/area")
public class BusinessAreaDictController {

	@Autowired
	private BusinessAreaDictService businessAreaDictService;
	
	/**
	 * 所有的业务领域ID和名字
	 * @return
	 */
	@GetMapping("/all")
	public Response<List<BusinessAreaDict>> getAllBusinessAreas() {
		return ResponseUtils.successResponse(businessAreaDictService.getAllBusinessAreas());
	}
	
	/**
	 * 增加业务领域
	 * @param businessAreaName
	 * @return
	 */
	@PostMapping("/add")
	public Response<?> addBusinessArea(@RequestParam(value="businessAreaName") String businessAreaName) {
		businessAreaDictService.insertBusinessArea(businessAreaName);
		return ResponseUtils.successResponse();
	}
	
	/**
	 * 更新业务领域
	 * @param businessAreaId
	 * @param newBusinessAreaName
	 * @return
	 */
	@PutMapping("/update")
	public Response<?> updateBusinessArea(@RequestParam(value="businessAreaId") Integer businessAreaId, @RequestParam(value="newBusinessAreaName") String newBusinessAreaName) {
		businessAreaDictService.updateBusinessArea(businessAreaId, newBusinessAreaName);
		return ResponseUtils.successResponse();
	}
	
	/**
	 * 删除业务领域
	 * @param businessAreaIds
	 * @return
	 */
	@DeleteMapping("/delete")
	public Response<?> deleteBusinessArea(@RequestParam(value="businessAreaIds") List<Integer> businessAreaIds) {
		businessAreaDictService.deleteBusinessAreas(businessAreaIds);
		return ResponseUtils.successResponse();
	}
}
