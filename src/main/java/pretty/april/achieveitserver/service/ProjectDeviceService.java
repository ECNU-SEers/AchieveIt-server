package pretty.april.achieveitserver.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import pretty.april.achieveitserver.entity.Project;
import pretty.april.achieveitserver.entity.ProjectDevice;
import pretty.april.achieveitserver.mapper.ProjectDeviceMapper;
import pretty.april.achieveitserver.request.device.CreateOrUpdateProjectDeviceRequest;
import pretty.april.achieveitserver.request.device.ShowProjectDeviceListRequest;

@Service
public class ProjectDeviceService extends ServiceImpl<ProjectDeviceMapper, ProjectDevice> {

	@Autowired
	private ProjectDeviceMapper projectDeviceMapper;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProjectService projectService;
	
	/**
	 * 新建项目设备
	 * @param request
	 * @return 新建的项目设备
	 * @throws Exception
	 */
	@Transactional
	public ProjectDevice createProjectDevice(CreateOrUpdateProjectDeviceRequest request) throws Exception {
//		1.验证该设备是否存在
		boolean projectDeviceIsExist = this.checkProjectDeviceExistByOuterIdAndProjectId(request.getOuterId(), request.getProjectId());
		if (projectDeviceIsExist) {
			throw new Exception("The device already exists in this project, please choose a new one");
		}
		
//		2.新建项目设备
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
	 * 搜索所有项目设备ID中包含某关键字的所有项目设备ID
	 * @param keyword
	 * @return 项目设备ID中包含该关键字的所有项目设备ID
	 */
	public List<String> searchProjectDeviceWithOuterIdIncludingKeyword(String keyword) {
		return this.baseMapper.selectByOuterIdLikeKeyword(keyword);
	}
	
	/**
	 * 列表展示项目设备ID为$outerId的设备信息
	 * @param outerId 设备ID
	 * @return 设备信息
	 */
	public ShowProjectDeviceListRequest showProjectDeviceList(String outerId) {
		ProjectDevice projectDevice = this.getProjectDeviceByOuterId(outerId);
		
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
	 * 通过项目设备ID查找项目设备
	 * @param outerId 设备ID
	 * @return 项目设备
	 */
	public ProjectDevice getProjectDeviceByOuterId(String outerId) {
		QueryWrapper<ProjectDevice> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(ProjectDevice::getOuterId, outerId);
		return this.getOne(queryWrapper);
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
		ProjectDevice primaryProjectDevice = this.getProjectDeviceByOuterId(request.getOuterId());
		ProjectDevice projectDevice = new ProjectDevice();
		BeanUtils.copyProperties(request, projectDevice);
		projectDevice.setId(primaryProjectDevice.getId());
		projectDevice.setState(primaryProjectDevice.getState());
		projectDevice.setReturnDate(primaryProjectDevice.getReturnDate());
		this.baseMapper.updateById(projectDevice);
		
		return projectDevice;
	}
	
	
//	public void removeProjectDevice(String outerId) {
//		ProjectDevice projectDevice = this.getProjectDeviceByOuterId(outerId);
//		
//	}
}
