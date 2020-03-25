package pretty.april.achieveitserver.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import pretty.april.achieveitserver.dto.PageDTO;
import pretty.april.achieveitserver.entity.LaborHour;
import pretty.april.achieveitserver.entity.User;
import pretty.april.achieveitserver.mapper.ActivityMapper;
import pretty.april.achieveitserver.mapper.LaborHourMapper;
import pretty.april.achieveitserver.mapper.ProjectFunctionMapper;
import pretty.april.achieveitserver.request.laborhour.CreateLaborHourRequest;
import pretty.april.achieveitserver.request.laborhour.RetrieveLaborHourRequest;
import pretty.april.achieveitserver.request.laborhour.ShowLaborHourListRequest;
import pretty.april.achieveitserver.request.laborhour.UpdateLaborHourRequest;

@Service
public class LaborHourService extends ServiceImpl<LaborHourMapper, LaborHour> {

	@Autowired
	private FunctionService functionService;
	
	@Autowired
	private ActivityService activityService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private ProjectFunctionMapper projectFunctionMapper;
	
	@Autowired
	private ActivityMapper activityMapper;
	
	/**
	 * 按照开始日期和结束日期查询某个用户的工时信息
	 * @param pageNo
	 * @param pageSize
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @param userId 用户ID
	 * @return 该用户在该时间范围内的所有工时信息
	 */
	public PageDTO<RetrieveLaborHourRequest> retrieveLaborHourByDates(Integer pageNo, Integer pageSize, LocalDate startDate, LocalDate endDate, Integer userId) {
		Page<LaborHour> page = new Page<>(pageNo, pageSize);
		QueryWrapper<LaborHour> queryWrapper = new QueryWrapper<LaborHour>();
		queryWrapper.lambda().eq(LaborHour::getUserId, userId).ge(LaborHour::getDate, startDate).le(LaborHour::getDate, endDate).orderByAsc(LaborHour::getDate);
		IPage<LaborHour> laborHours = this.baseMapper.selectPage(page, queryWrapper);
		List<RetrieveLaborHourRequest> laborHourList = new ArrayList<RetrieveLaborHourRequest>();
		for (LaborHour laborHour: laborHours.getRecords()) {
			RetrieveLaborHourRequest request = new RetrieveLaborHourRequest();
			BeanUtils.copyProperties(laborHour, request);
			request.setFunctionName(functionService.getById(laborHour.getFunctionId()).getName());
			request.setActivityName(activityService.getById(laborHour.getActivityId()).getName());
			laborHourList.add(request);
		}
		return new PageDTO<RetrieveLaborHourRequest>(laborHours.getCurrent(), laborHours.getSize(), laborHours.getTotal(), laborHourList);
	}
	
	/**
	 * 新建工时信息
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	public LaborHour createLaborHour(CreateLaborHourRequest request) throws Exception {
//		1.如果工时填写的日期在今天日期的3天之前，则不能提交
		LocalDate localdate1 = request.getDate();
		LocalDate localdate2 = LocalDate.now(Clock.systemUTC());
		Period period = Period.between(localdate1, localdate2);
		if (period.getDays() > 3) {
			throw new Exception("You can only submit your labor hour info with three days.");
		}
		
//		2.新建工时信息
		LaborHour laborHour = new LaborHour();
		BeanUtils.copyProperties(request, laborHour);
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getByUsername(username);
		laborHour.setUserId(user.getId());
		laborHour.setProjectId(functionService.getById(request.getSubfunctionId()).getProjectId());
		laborHour.setActivityId(request.getSubactivityId());
		laborHour.setFunctionId(request.getSubfunctionId());
		laborHour.setSubmissionDate(LocalDateTime.now(Clock.systemUTC()));
		laborHour.setState("待审核");
		this.baseMapper.insert(laborHour);
		return laborHour;
	}
	
	/**
	 * 列表展示某个用户的所有工时信息
	 * @param pageNo
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	public PageDTO<ShowLaborHourListRequest> showList(Integer pageNo, Integer pageSize, Integer userId) {
		Page<LaborHour> page = new Page<>(pageNo, pageSize);
		QueryWrapper<LaborHour> queryWrapper = new QueryWrapper<LaborHour>();
		queryWrapper.lambda().eq(LaborHour::getUserId, userId).orderByAsc(LaborHour::getDate);
		IPage<LaborHour> laborHours = this.baseMapper.selectPage(page, queryWrapper);
		List<ShowLaborHourListRequest> laborHourList = new ArrayList<ShowLaborHourListRequest>();
		for (LaborHour laborHour: laborHours.getRecords()) {
			ShowLaborHourListRequest request = new ShowLaborHourListRequest();
			BeanUtils.copyProperties(laborHour, request);
			Integer subfunctionId = laborHour.getFunctionId();
			Integer functionId = projectFunctionMapper.selectById(subfunctionId).getParentId();
			request.setFunctionId(functionId);
			request.setFunctionName(projectFunctionMapper.selectById(functionId).getName());
			request.setSubfunctionId(subfunctionId);
			request.setSubfunctionName(projectFunctionMapper.selectById(subfunctionId).getName());
			
			Integer subactivityId = laborHour.getActivityId();
			Integer activityId = activityMapper.selectById(subactivityId).getParentId();
			request.setActivityId(activityId);
			request.setActivityName(activityMapper.selectById(activityId).getName());
			request.setSubactivityId(subactivityId);
			request.setSubactivityName(activityMapper.selectById(subactivityId).getName());
			
			laborHourList.add(request);
		}
		return new PageDTO<ShowLaborHourListRequest>(laborHours.getCurrent(), laborHours.getSize(), laborHours.getTotal(), laborHourList);
	}
	
	/**
	 * 更新工时信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public LaborHour updateLaborHour(UpdateLaborHourRequest request) throws Exception {
		if (projectService.getById(this.getById(request.getId()).getProjectId()).getState().equals("已审核")) {
			throw new Exception("You cannot update the info while reviewing.");
		}
		LaborHour laborHour = this.baseMapper.selectById(request.getId());
		BeanUtils.copyProperties(request, laborHour);
		laborHour.setActivityId(request.getSubactivityId());
		laborHour.setFunctionId(request.getSubfunctionId());
		laborHour.setSubmissionDate(LocalDateTime.now(Clock.systemUTC()));
		this.baseMapper.updateById(laborHour);
		return laborHour;
	}
}
