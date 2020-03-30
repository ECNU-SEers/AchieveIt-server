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

import pretty.april.achieveitserver.dto.PageDTO;
import pretty.april.achieveitserver.dto.Response;
import pretty.april.achieveitserver.request.device.CreateOrUpdateProjectDeviceRequest;
import pretty.april.achieveitserver.request.device.RetrieveDeviceInspectionRequest;
import pretty.april.achieveitserver.request.device.ShowProjectDeviceListRequest;
import pretty.april.achieveitserver.service.ProjectDeviceService;
import pretty.april.achieveitserver.utils.ResponseUtils;

@RestController
@RequestMapping("/api/device")
public class ProjectDeviceController {

	@Autowired
	private ProjectDeviceService projectDeviceService;
	
	/**
	 * 利用关键字搜索项目资产（010801）
	 * @param keyword
	 * @param projectId 项目表ID
	 * @return 设备ID中包含关键字的所有设备ID
	 */
	@GetMapping("/search")
	public Response<List<String>> searchDeviceUsingKeyword(@RequestParam(value="keyword") String keyword,
														   @RequestParam(value="projectId") Integer projectId) {
		return ResponseUtils.successResponse(projectDeviceService.searchProjectDeviceWithOuterIdIncludingKeyword(keyword, projectId));
	}
	
	/**
	 * 搜索过程中选择某个资产查看信息（010801）
	 * @param outerId 设备ID
	 * @param projectId 项目表ID
	 * @return 设备信息列表
	 */
	@GetMapping("/show/detail")
	public Response<ShowProjectDeviceListRequest> showProjectDeviceList(@RequestParam(value="outerId") String outerId, 
																		@RequestParam(value="projectId") Integer projectId) {
		return ResponseUtils.successResponse(projectDeviceService.showProjectDeviceList(outerId, projectId));
	}
	
	/**
	 * 新增项目资产（010802）
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/create")
	public Response<?> createProjectDevice(@RequestBody CreateOrUpdateProjectDeviceRequest request) throws Exception {
		projectDeviceService.createProjectDevice(request);
		return ResponseUtils.successResponse();
	}
	
	/**
	 * 展示某个项目的所有设备信息，并利用关键字搜索设备的outerId（01080301）
	 * @param pageNo
	 * @param pageSize
	 * @param projectId 项目表ID
	 * @return 该项目的所有设备信息
	 */
	@GetMapping("/show/all/details")
	public Response<PageDTO<ShowProjectDeviceListRequest>> showDevices(@RequestParam(value="pageNo") Integer pageNo,
													 @RequestParam(value="pageSize") Integer pageSize,
													 @RequestParam(value="projectId") Integer projectId,
													 @RequestParam(value="keyword") String keyword) {
		return ResponseUtils.successResponse(projectDeviceService.showDevices(pageNo, pageSize, projectId, keyword));
	}
	
	/**
	 * 不分页展示某个设备的所有检查信息（01080302）
	 * @param deviceId 设备表ID
	 * @return 该设备的所有检查信息
	 */
	@GetMapping("/inspect")
	public Response<List<RetrieveDeviceInspectionRequest>> retrieveDeviceInspection(@RequestParam(value="deviceOuterId") String deviceOuterId, 
																					@RequestParam(value="projectId") Integer projectId) {
		return ResponseUtils.successResponse(projectDeviceService.retrieveDeviceInspection(deviceOuterId, projectId));
	}
	
	/**
	 * 分页展示某个设备的所有检查信息（01080302）
	 * @param pageNo
	 * @param pageSize
	 * @param deviceId 设备表ID
	 * @return 该设备的所有检查信息
	 */
	@GetMapping("/inspect/by/page")
	public Response<PageDTO<RetrieveDeviceInspectionRequest>> retrieveDeviceInspectionByPage(@RequestParam(value="pageNo") Integer pageNo,
													 @RequestParam(value="pageSize") Integer pageSize,
													 @RequestParam(value="deviceOuterId") String deviceOuterId, 
													 @RequestParam(value="projectId") Integer projectId) {
		return ResponseUtils.successResponse(projectDeviceService.retrieveDeviceInspectionByPage(pageNo, pageSize, deviceOuterId, projectId));
	}
	
	/**
	 * 更新设备信息（01080303）
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/update")
	public Response<?> updateProjectDevice(@RequestBody CreateOrUpdateProjectDeviceRequest request) throws Exception {
		projectDeviceService.updateProjectDeviceInfo(request);
		return ResponseUtils.successResponse();
	}
	
	/**
	 * 利用设备ID移除设备（01080304：不做）
	 * @param outerId
	 * @return
	 * @throws Exception
	 */
//	@PutMapping("/remove")
//	public Response<?> removeProjectDevice(@RequestParam(value="outerId") String outerId) throws Exception {
//		projectDeviceService.removeProjectDevice(outerId);
//		return ResponseUtils.successResponse();
//	}
}
