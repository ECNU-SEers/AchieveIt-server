package pretty.april.achieveitserver.controller;

import java.time.LocalDate;

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
			 																	@RequestParam(value="startDate") LocalDate startDate,
			 																	@RequestParam(value="endDate") LocalDate endDate,
			 																	@RequestParam(value="userId") Integer userId) {
		return ResponseUtils.successResponse(laborHourService.retrieveLaborHourByDates(pageNo, pageSize, startDate, endDate, userId));
	}
	
	/**
	 * 新增工时信息（030102）
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/create")
	public Response<?> createProject(@RequestBody CreateLaborHourRequest request) throws Exception {
		laborHourService.createLaborHour(request);
		return ResponseUtils.successResponse();
	}
	
	/**
	 * 列表展示工时信息（03010301）
	 * @param pageNo
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	@GetMapping("show/list")
	public Response<PageDTO<ShowLaborHourListRequest>> showList(@RequestParam(value="pageNo") Integer pageNo,
			 													@RequestParam(value="pageSize") Integer pageSize,
			 													@RequestParam(value="userId") Integer userId) {
		return ResponseUtils.successResponse(laborHourService.showList(pageNo, pageSize, userId));
	}
	
	/**
	 * 编辑工时信息（03010302）
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/update")
	public Response<?> updateLaborHour(@RequestBody UpdateLaborHourRequest request) throws Exception {
		laborHourService.updateLaborHour(request);
		return ResponseUtils.successResponse();
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
																					  @RequestParam(value="startDate") LocalDate startDate,
																					  @RequestParam(value="endDate") LocalDate endDate,
																					  @RequestParam(value="userId") Integer userId) {
		return ResponseUtils.successResponse(laborHourService.retrieveLaborHourOfSubordinate(pageNo, pageSize, startDate, endDate, userId));
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
																					  @RequestParam(value="pageSize") Integer pageSize,
																					  @RequestParam(value="userId") Integer userId) {
		return ResponseUtils.successResponse(laborHourService.showSubordinateLists(pageNo, pageSize, userId));
	}

	/**
	 * 审核工时信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/accept")
	public Response<?> acceptLaborHourInfo(@RequestBody RetrieveLaborHourRequest request) throws Exception {
		laborHourService.acceptLaborHourInfo(request);
		return ResponseUtils.successResponse();
	}
	
	/**
	 * 退回工时信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/return")
	public Response<?> returnLaborHourInfo(@RequestBody RetrieveLaborHourRequest request) throws Exception {
		laborHourService.returnLaborHourInfo(request);
		return ResponseUtils.successResponse();
	}
	
}
