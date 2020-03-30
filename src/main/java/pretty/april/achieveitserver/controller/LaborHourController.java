package pretty.april.achieveitserver.controller;

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
import pretty.april.achieveitserver.enums.ErrorCode;
import pretty.april.achieveitserver.request.laborhour.CreateLaborHourRequest;
import pretty.april.achieveitserver.request.laborhour.RetrieveLaborHourRequest;
import pretty.april.achieveitserver.request.laborhour.ShowLaborHourListRequest;
import pretty.april.achieveitserver.request.laborhour.ShowSubordinateLaborHourListRequest;
import pretty.april.achieveitserver.request.laborhour.UpdateLaborHourRequest;
import pretty.april.achieveitserver.service.LaborHourService;
import pretty.april.achieveitserver.utils.ResponseUtils;

@RestController
@RequestMapping("/api/laborhour")
public class LaborHourController {
	
	@Autowired
	private LaborHourService laborHourService;
	
	/**
	 * 用户查询一段时间内的工时信息（030101）
	 * @param pageNo
	 * @param pageSize
	 * @param startDate
	 * @param endDate
	 * @param userId
	 * @return
	 */
	@GetMapping("/search")
	public Response<PageDTO<RetrieveLaborHourRequest>> retrieveLaborHourByDates(@RequestParam(value="pageNo") Integer pageNo,
			 																	@RequestParam(value="pageSize") Integer pageSize,
			 																	@RequestParam(value="startDateTimestamp") Long startDateTimestamp,
			 																	@RequestParam(value="endDateTimestamp") Long endDateTimestamp) {
		return ResponseUtils.successResponse(laborHourService.retrieveLaborHourByDates(pageNo, pageSize, startDateTimestamp, endDateTimestamp));
	}
	
	/**
	 * 新增工时信息（030102）
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/create")
	public Response<?> createProject(@RequestBody CreateLaborHourRequest request) throws Exception {
		String result = laborHourService.createLaborHour(request);
		if ("success".equals(result)) {
			return ResponseUtils.successResponse();
		} else {
			return ResponseUtils.errorResponseWithMessage(ErrorCode.STATE_ERROR, result);
		}
	}
	
	/**
	 * 列表展示工时信息（03010301）
	 * @param pageNo
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	@GetMapping("/show/list")
	public Response<PageDTO<ShowLaborHourListRequest>> showList(@RequestParam(value="pageNo") Integer pageNo,
			 													@RequestParam(value="pageSize") Integer pageSize) {
		return ResponseUtils.successResponse(laborHourService.showList(pageNo, pageSize));
	}
	
	/**
	 * 编辑工时信息（03010302）
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/update")
	public Response<?> updateLaborHour(@RequestBody UpdateLaborHourRequest request) throws Exception {
		String result = laborHourService.updateLaborHour(request);
		if ("success".equals(result)) {
			return ResponseUtils.successResponse();
		} else {
			return ResponseUtils.errorResponseWithMessage(ErrorCode.STATE_ERROR, result);
		}
	}
	
	/**
	 * 用户查询一段时间内其下属的工时信息（030201）
	 * @param pageNo
	 * @param pageSize
	 * @param startDate
	 * @param endDate
	 * @param userId
	 * @return
	 */
	@GetMapping("/search/subordinate")
	public Response<PageDTO<RetrieveLaborHourRequest>> retrieveLaborHourOfSubordinate(@RequestParam(value="pageNo") Integer pageNo,
																					  @RequestParam(value="pageSize") Integer pageSize,
																					  @RequestParam(value="startDateTimestamp") Long startDateTimestamp,
																					  @RequestParam(value="endDateTimestamp") Long endDateTimestamp) {
		return ResponseUtils.successResponse(laborHourService.retrieveLaborHourOfSubordinate(pageNo, pageSize, startDateTimestamp, endDateTimestamp));
	}
	
	/**
	 * 列表展示某个用户的所有下属的工时信息（03020201）
	 * @param pageNo
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	@GetMapping("/show/subordinate/list")
	public Response<PageDTO<ShowSubordinateLaborHourListRequest>> showSubordinateList(@RequestParam(value="pageNo") Integer pageNo,
																					  @RequestParam(value="pageSize") Integer pageSize) {
		return ResponseUtils.successResponse(laborHourService.showSubordinateLists(pageNo, pageSize));
	}

	/**
	 * 审核工时信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/accept")
	public Response<?> acceptLaborHourInfo(@RequestParam(value="id") Integer id) throws Exception {
		String result = laborHourService.acceptLaborHourInfo(id);
		if ("success".equals(result)) {
			return ResponseUtils.successResponse();
		} else {
			return ResponseUtils.errorResponseWithMessage(ErrorCode.STATE_ERROR, result);
		}
	}
	
	/**
	 * 退回工时信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/return")
	public Response<?> returnLaborHourInfo(@RequestParam(value="id") Integer id) throws Exception {
		String result = laborHourService.returnLaborHourInfo(id);
		if ("success".equals(result)) {
			return ResponseUtils.successResponse();
		} else {
			return ResponseUtils.errorResponseWithMessage(ErrorCode.STATE_ERROR, result);
		}
	}
	
}
