package pretty.april.achieveitserver.service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
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
import pretty.april.achieveitserver.request.laborhour.ShowSubordinateLaborHourListRequest;
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
	
	@Autowired
	private LaborHourMapper laborHourMapper;
	
	/**
	 * 按照开始日期和结束日期查询某个用户的工时信息
	 * @param pageNo
	 * @param pageSize
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @param userId 用户ID
	 * @return 该用户在该时间范围内的所有工时信息
	 */
	public PageDTO<RetrieveLaborHourRequest> retrieveLaborHourByDates(Integer pageNo, Integer pageSize, Long startDateTimestamp, Long endDateTimestamp) {
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getByUsername(username);
        Integer userId = user.getId();
        
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
        String start = sdf.format(new Date(Long.parseLong(String.valueOf(startDateTimestamp))));
        LocalDate startDate = LocalDate.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String end = sdf.format(new Date(Long.parseLong(String.valueOf(endDateTimestamp))));
        LocalDate endDate = LocalDate.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd"));      
        
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
			request.setSubmissionDate(laborHour.getSubmissionDate().toLocalDate());
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
		LocalDate localdate2 = LocalDate.now();
		Period period = Period.between(localdate1, localdate2);
		if (period.getDays() > 3) {
			throw new Exception("You can only submit your labor hour info with three days.");
		}
		
//		2.一天之内工时不超过24小时
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getByUsername(username);
        Integer userId = user.getId();
//      找到这个人在填写的工时日期（loacldate1）当天的工时总和
        List<LaborHour> dayRecords = this.baseMapper.selectByUserIdAndDate(userId, localdate1);
        long allSeconds = 0;
        for (LaborHour record: dayRecords) {
        	Duration duration = Duration.between(record.getStartTime(), record.getEndTime());
        	allSeconds = allSeconds + duration.getSeconds();
        }
        allSeconds = allSeconds + Duration.between(request.getStartTime(), request.getEndTime()).getSeconds();
		if (allSeconds > 86400) {
			throw new Exception("The extremity of labor hour is 24 hours.");
		}
		
//		3.工时的开始到结束时间段不能重叠
		boolean flag = true;
		for (LaborHour record: dayRecords) {
			if (request.getStartTime().compareTo(record.getStartTime())>0 && request.getStartTime().compareTo(record.getEndTime())<0) {
				flag = false;
				break;
			}
			if (request.getEndTime().compareTo(record.getStartTime())>0 && request.getEndTime().compareTo(record.getEndTime())<0) {
				flag = false;
				break;
			}
		}
		if (!flag) {
			throw new Exception("The time period cannot overlap.");
		}
		
//		4.新建工时信息
		LaborHour laborHour = new LaborHour();
		BeanUtils.copyProperties(request, laborHour);
		laborHour.setUserId(userId);
		laborHour.setProjectId(functionService.getById(request.getSubfunctionId()).getProjectId());
		laborHour.setActivityId(request.getSubactivityId());
		laborHour.setFunctionId(request.getSubfunctionId());
		laborHour.setSubmissionDate(LocalDateTime.now());
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
	public PageDTO<ShowLaborHourListRequest> showList(Integer pageNo, Integer pageSize) {
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getByUsername(username);
        Integer userId = user.getId();
		
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
			
			request.setSubmissionDate(laborHour.getSubmissionDate().toLocalDate());
			
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
		if (this.getById(request.getId()).getState().equals("已通过")) {
			throw new Exception("You cannot update the info if passed.");
		}
		LaborHour laborHour = this.baseMapper.selectById(request.getId());
		BeanUtils.copyProperties(request, laborHour);
		laborHour.setActivityId(request.getSubactivityId());
		laborHour.setFunctionId(request.getSubfunctionId());
		laborHour.setSubmissionDate(LocalDateTime.now());
		this.baseMapper.updateById(laborHour);
		return laborHour;
	}
	
	/**
	 * 查询某个时间范围内某个领导的所有项目下级的工时记录
	 * @param startDate
	 * @param endDate
	 * @param userId
	 * @param page
	 * @return
	 */
	public Page<LaborHour> getLaborHourOfSubordinate(LocalDate startDate, LocalDate endDate, Integer userId, Page<LaborHour> page) {
		return page.setRecords(this.baseMapper.selectByLeaderIdAndDates(startDate, endDate, userId, page));
	}
	
	/**
	 * 按照开始日期和结束日期查询某个用户的下属的工时信息
	 * @param pageNo
	 * @param pageSize
	 * @param startDate
	 * @param endDate
	 * @param userId
	 * @return
	 */
	public PageDTO<RetrieveLaborHourRequest> retrieveLaborHourOfSubordinate(Integer pageNo, Integer pageSize, Long startDateTimestamp, Long endDateTimestamp) {
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getByUsername(username);
        Integer userId = user.getId();
        
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
        String start = sdf.format(new Date(Long.parseLong(String.valueOf(startDateTimestamp))));
        LocalDate startDate = LocalDate.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String end = sdf.format(new Date(Long.parseLong(String.valueOf(endDateTimestamp))));
        LocalDate endDate = LocalDate.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd")); 
		
		Page<LaborHour> page = this.getLaborHourOfSubordinate(startDate, endDate, userId, new Page<LaborHour>(pageNo, pageSize));
		List<RetrieveLaborHourRequest> laborHourDetails = new ArrayList<RetrieveLaborHourRequest>();
		for (LaborHour laborHour: page.getRecords()) {
			RetrieveLaborHourRequest request = new RetrieveLaborHourRequest();
			BeanUtils.copyProperties(laborHour, request);
			request.setFunctionName(functionService.getById(laborHour.getFunctionId()).getName());
			request.setActivityName(activityService.getById(laborHour.getActivityId()).getName());
			request.setSubmissionDate(laborHour.getSubmissionDate().toLocalDate());
			laborHourDetails.add(request);
		}
		return new PageDTO<RetrieveLaborHourRequest>(page.getCurrent(), page.getSize(), page.getTotal(), laborHourDetails);
	}
	
	/**
	 * 查询某个领导的所有项目下级的工时记录
	 * @param userId
	 * @param page
	 * @return
	 */
	public Page<LaborHour> showSubordinateList(Integer userId, Page<LaborHour> page) {
		return page.setRecords(this.baseMapper.selectByLeaderId(userId, page));
	}
	
	/**
	 * 列表展示某个用户的所有下属的工时信息（待审核优先）
	 * @param pageNo
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	public PageDTO<ShowSubordinateLaborHourListRequest> showSubordinateLists(Integer pageNo, Integer pageSize) {
//		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User user = userService.getByUsername(username);
        Integer userId = 1;
		
		Page<LaborHour> page = this.showSubordinateList(userId, new Page<LaborHour>(pageNo, pageSize));
		List<ShowSubordinateLaborHourListRequest> laborHourDetails = new ArrayList<ShowSubordinateLaborHourListRequest>();
		for (LaborHour laborHour: page.getRecords()) {
			ShowSubordinateLaborHourListRequest request = new ShowSubordinateLaborHourListRequest();
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
			
			request.setSubmitterName(userService.getById(laborHour.getUserId()).getUsername());
			request.setProjectName(projectService.getById(laborHour.getProjectId()).getName());
			
			request.setSubmissionDate(laborHour.getSubmissionDate().toLocalDate());
			
			laborHourDetails.add(request);
		}
		return new PageDTO<ShowSubordinateLaborHourListRequest>(page.getCurrent(), page.getSize(), page.getTotal(), laborHourDetails);
	}
	
	/**
	 * 审核通过某个工时信息
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	public LaborHour acceptLaborHourInfo(RetrieveLaborHourRequest request) throws Exception {
		if (request.getState().equals("已通过")) {
			throw new Exception("The record has already been passed.");
		}
		LaborHour laborHour = this.baseMapper.selectById(request.getId());
		laborHour.setState("已通过");
		this.baseMapper.updateById(laborHour);
		return  laborHour;
	}
	
	/**
	 * 退回某个工时信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public LaborHour returnLaborHourInfo(RetrieveLaborHourRequest request) throws Exception {
		if (request.getState().equals("已退回")) {
			throw new Exception("The record has already been returned.");
		}
		LaborHour laborHour = this.baseMapper.selectById(request.getId());
		laborHour.setState("已退回");
		this.baseMapper.updateById(laborHour);
		return  laborHour;
	}
	
}
