package pretty.april.achieveitserver.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import pretty.april.achieveitserver.dto.PageDTO;
import pretty.april.achieveitserver.entity.DeviceInspection;
import pretty.april.achieveitserver.entity.Project;
import pretty.april.achieveitserver.entity.ProjectDevice;
import pretty.april.achieveitserver.mapper.DeviceInspectionMapper;
import pretty.april.achieveitserver.mapper.ProjectDeviceMapper;
import pretty.april.achieveitserver.request.device.CreateOrUpdateProjectDeviceRequest;
import pretty.april.achieveitserver.request.device.RetrieveDeviceInspectionRequest;
import pretty.april.achieveitserver.request.device.ShowProjectDeviceListRequest;

@Service
public class ProjectDeviceService extends ServiceImpl<ProjectDeviceMapper, ProjectDevice> {

	@Autowired
	private ProjectDeviceMapper projectDeviceMapper;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private DeviceInspectionMapper deviceInspectionMapper;
	
	/**
	 * 新建项目设备
	 * @param request
	 * @return 新建的项目设备
	 * @throws Exception
	 */
	@Transactional
	public ProjectDevice createProjectDevice(CreateOrUpdateProjectDeviceRequest request) throws Exception {
//		1.验证该设备是否已经存在于本项目中
		boolean projectDeviceIsExist = this.checkProjectDeviceExistByOuterIdAndProjectId(request.getOuterId(), request.getProjectId());
		if (projectDeviceIsExist) {
			throw new Exception("The device already exists in this project, please choose a new one.");
		}
//		2.验证该设备是否正在被其他项目使用
		List<String> allStates = this.checkProjectDeviceFreeByOuterId(request.getOuterId());
		for (String state: allStates) {
			if (state.equals("已领取")) {
				throw new Exception("The device hasn't been returned, thus unavailable.");
			}
		}
		
//		3.新建项目设备
		ProjectDevice projectDevice = new ProjectDevice();
		BeanUtils.copyProperties(request, projectDevice);
		projectDevice.setState("已领取");
		projectDeviceMapper.insert(projectDevice);
		return projectDevice;
	}
	
	/**
	 * 根据设备ID和项目表ID检查项目设备是否存在
	 * @param outerId 设备ID
	 * @param projectId 项目表ID
	 * @return true代表存在
	 */
	public boolean checkProjectDeviceExistByOuterIdAndProjectId(String outerId, Integer projectId) {
		int row = this.baseMapper.selectCountByOuterIdAndProjectId(outerId, projectId);
		return row > 0;
	}
	
	/**
	 * 根据设备ID查询该设备的所有历史状态
	 * @param outerId 设备ID
	 * @return 所有状态
	 */
	public List<String> checkProjectDeviceFreeByOuterId(String outerId) {
		return this.baseMapper.selectStatesByOuterId(outerId);
	}
	
	/**
	 * 搜索某个项目中设备ID中包含某关键字的所有项目设备ID
	 * @param keyword
	 * @param projectId 项目表ID
	 * @return 该项目中设备ID中包含该关键字的所有项目设备ID
	 */
	public List<String> searchProjectDeviceWithOuterIdIncludingKeyword(String keyword, Integer projectId) {
		return this.baseMapper.selectByOuterIdLikeKeyword(keyword, projectId);
	}
	
	/**
	 * 列表展示某个项目中项目设备ID为$outerId的设备信息
	 * @param outerId 设备ID
	 * @param projectId 项目表ID
	 * @return 设备信息
	 */
	public ShowProjectDeviceListRequest showProjectDeviceList(String outerId, Integer projectId) {
		ProjectDevice projectDevice = this.getProjectDeviceByOuterIdAndProjectId(outerId, projectId);
		
		ShowProjectDeviceListRequest projectDeviceList = new ShowProjectDeviceListRequest();
		BeanUtils.copyProperties(projectDevice, projectDeviceList);
		projectDeviceList.setManagerName(userService.getById(projectDevice.getManagerId()).getUsername());
		if (projectDeviceList.getState().equals("已归还")) {
			projectDeviceList.setReturnDate(projectDevice.getReturnDate().toString());
		} else if (projectDeviceList.getState().equals("已领取")) {
			projectDeviceList.setReturnDate("暂无");
		}
		
		return projectDeviceList;
	}
	
	/**
	 * 通过项目设备ID和项目表ID查找项目设备
	 * @param outerId 设备ID
	 * @param projectId 项目表ID
	 * @return 项目设备
	 */
	public ProjectDevice getProjectDeviceByOuterIdAndProjectId(String outerId, Integer projectId) {
		QueryWrapper<ProjectDevice> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(ProjectDevice::getOuterId, outerId).eq(ProjectDevice::getProjectId, projectId);
		return this.getOne(queryWrapper);
	}
	
	/**
	 * 展示某个项目的所有设备，利用关键字搜索某个设备的outerId
	 * @param pageNo
	 * @param pageSize
	 * @param projectId 项目ID
	 * @return 该项目的所有设备信息列表
	 */
	public PageDTO<ShowProjectDeviceListRequest> showDevices(Integer pageNo, Integer pageSize, Integer projectId, String keyword) {
		Page<ProjectDevice> page = new Page<>(pageNo, pageSize);
		QueryWrapper<ProjectDevice> queryWrapper = new QueryWrapper<ProjectDevice>();
		queryWrapper.eq("project_id", projectId).like("outer_id", keyword).orderByAsc("start_date");
		IPage<ProjectDevice> devices = projectDeviceMapper.selectPage(page, queryWrapper);
		List<ShowProjectDeviceListRequest> deviceLists = new ArrayList<ShowProjectDeviceListRequest>();
		for (ProjectDevice device: devices.getRecords()) {
			deviceLists.add(this.showProjectDeviceList(device.getOuterId(), device.getProjectId()));
		}
		return new PageDTO<ShowProjectDeviceListRequest>(page.getCurrent(), page.getSize(), page.getTotal(), deviceLists);
	}
	
	/**
	 * 查询设备ID为$deviceId的设备的所有检查记录
	 * @param deviceId
	 * @return 某设备的所有检查记录
	 */
	public List<RetrieveDeviceInspectionRequest> retrieveDeviceInspection(String deviceOuterId, Integer projectId) {
		Integer deviceId = projectDeviceMapper.selectByOuterIdAndProjectId(deviceOuterId, projectId).getId();
		List<DeviceInspection> deviceInspections = deviceInspectionMapper.selectInfoByDeviceId(deviceId);
		List<RetrieveDeviceInspectionRequest> deviceInspectionInfo = new ArrayList<>();
		for (DeviceInspection deviceInspection: deviceInspections) {
			RetrieveDeviceInspectionRequest deviceInspectionRequest = new RetrieveDeviceInspectionRequest();
			deviceInspectionRequest.setInspectDate(deviceInspection.getInspectDate());
			if (deviceInspection.getIntact().equals(true)) {
				deviceInspectionRequest.setIntact("完好");
			} else {
				deviceInspectionRequest.setIntact("损坏");
			}
			if (deviceInspection.getRemark()==null || deviceInspection.getRemark().equals("")) {
				deviceInspectionRequest.setRemark("暂无");
			} else {
				deviceInspectionRequest.setRemark(deviceInspection.getRemark());
			}
			deviceInspectionInfo.add(deviceInspectionRequest);
		}
		return deviceInspectionInfo;
	}
	
	/**
	 * 分页展示某个设备的所有检查记录
	 * @param pageNo
	 * @param pageSize
	 * @param deviceId 项目设备表ID
	 * @return 该设备的所有检查记录
	 */
	public PageDTO<RetrieveDeviceInspectionRequest> retrieveDeviceInspectionByPage(Integer pageNo, Integer pageSize, String deviceOuterId, Integer projectId) {
		Integer deviceId = projectDeviceMapper.selectByOuterIdAndProjectId(deviceOuterId, projectId).getId();
		Page<DeviceInspection> page = new Page<>(pageNo, pageSize);
		QueryWrapper<DeviceInspection> queryWrapper = new QueryWrapper<DeviceInspection>();
		queryWrapper.eq("device_id", deviceId).orderByAsc("inspect_date");
		IPage<DeviceInspection> deviceInspections = deviceInspectionMapper.selectPage(page, queryWrapper);
		List<RetrieveDeviceInspectionRequest> deviceInspectionLists = new ArrayList<RetrieveDeviceInspectionRequest>();
		for (DeviceInspection deviceInspection: deviceInspections.getRecords()) {
			RetrieveDeviceInspectionRequest deviceInspectionRequest = new RetrieveDeviceInspectionRequest();
			deviceInspectionRequest.setInspectDate(deviceInspection.getInspectDate());
			if (deviceInspection.getIntact()) {
				deviceInspectionRequest.setIntact("完好");
			} else {
				deviceInspectionRequest.setIntact("损坏");
			}
			if (deviceInspection.getRemark()==null || deviceInspection.getRemark().equals("")) {
				deviceInspectionRequest.setRemark("暂无");
			} else {
				deviceInspectionRequest.setRemark(deviceInspection.getRemark());
			}
			deviceInspectionLists.add(deviceInspectionRequest);
		}
		return new PageDTO<RetrieveDeviceInspectionRequest>(page.getCurrent(), page.getSize(), page.getTotal(), deviceInspectionLists);
	}
	
	/**
	 * 更新项目设备
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public ProjectDevice updateProjectDeviceInfo(CreateOrUpdateProjectDeviceRequest request) throws Exception {
//		1.判断项目是否“结束”或“已归档”，若是，则不能修改设备信息
		Project project = projectService.getById(request.getProjectId());
		if (project.getState().equals("结束") || project.getState().equals("已归档")) {
			throw new Exception("The project already expires, device info cannot be updated, please choose a new one.");
		}
//		2.更新项目设备信息
		ProjectDevice primaryProjectDevice = this.getProjectDeviceByOuterIdAndProjectId(request.getOuterId(), request.getProjectId());
		ProjectDevice projectDevice = new ProjectDevice();
		BeanUtils.copyProperties(request, projectDevice);
		projectDevice.setId(primaryProjectDevice.getId());
		projectDevice.setState(primaryProjectDevice.getState());
		projectDevice.setReturnDate(primaryProjectDevice.getReturnDate());
		this.baseMapper.updateById(projectDevice);
		
		return projectDevice;
	}
	
	/**
	 * 利用设备ID移除设备
	 * @param outerId 设备ID
	 * @param projectId 项目表ID
	 * @return
	 * @throws Exception
	 */
	public ProjectDevice removeProjectDevice(String outerId, Integer projectId) throws Exception {
		ProjectDevice projectDevice = this.getProjectDeviceByOuterIdAndProjectId(outerId, projectId);
		if (projectDevice.getState().equals("已归还")) {
			throw new Exception("The device cannot be removed, please choose a new one.");
		}
		this.baseMapper.deleteById(projectDevice.getId());
		return projectDevice;
	}
	
	/**
	 * 归还项目设备
	 * @param outerId 设备ID
	 * @param projectId 项目表ID
	 * @return
	 * @throws Exception
	 */
	public ProjectDevice returnProjectDevice(String outerId, Integer projectId) throws Exception {
		ProjectDevice projectDevice = this.getProjectDeviceByOuterIdAndProjectId(outerId, projectId);
		if (projectDevice.getState().equals("已归还")) {
			throw new Exception("The device has already returned, please choose another one.");
		}
		projectDevice.setState("已归还");
		this.baseMapper.updateById(projectDevice);
		return projectDevice;
	}
}
