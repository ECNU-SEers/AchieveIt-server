package pretty.april.achieveitserver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pretty.april.achieveitserver.dto.Response;
import pretty.april.achieveitserver.request.activity.ValueLabelChildren;
import pretty.april.achieveitserver.service.ActivityService;
import pretty.april.achieveitserver.utils.ResponseUtils;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {

	@Autowired
	private ActivityService activityService;
	
	/**
	 * 所有一级活动和其对应的二级活动
	 * @return
	 */
	@GetMapping("/all")
	public Response<List<ValueLabelChildren>> getAllActivities() {
		return ResponseUtils.successResponse(activityService.getAllActivities());
	}
}
