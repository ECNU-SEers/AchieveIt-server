package pretty.april.achieveitserver.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import pretty.april.achieveitserver.entity.Activity;
import pretty.april.achieveitserver.mapper.ActivityMapper;
import pretty.april.achieveitserver.request.activity.RetrieveActivityRequest;
import pretty.april.achieveitserver.request.activity.ValueLabelChildren;

@Service
public class ActivityService extends ServiceImpl<ActivityMapper, Activity> {

	public Activity getById(Integer id) {
		return this.baseMapper.selectById(id);
	}
	
	/**
	 * 查询所有一级活动
	 * @return
	 */
	public List<RetrieveActivityRequest> getFirstLevelActivity() {
		return this.baseMapper.selectAllFirstLevelActivities();
	}
	
	/**
	 * 查询某个一级活动的所有二级活动
	 * @param parentId
	 * @return
	 */
	public List<RetrieveActivityRequest> getSecondLevelActivity(Integer parentId) {
		return this.baseMapper.selectAllSecondLevelActivities(parentId);
	}
	
//	public Map<RetrieveActivityRequest, List<RetrieveActivityRequest>> getAllActivities() {
//		Map<RetrieveActivityRequest, List<RetrieveActivityRequest>> allActivities = new HashMap<RetrieveActivityRequest, List<RetrieveActivityRequest>>();
//		List<RetrieveActivityRequest> firstLevelActivities = this.getFirstLevelActivity();
//		for (RetrieveActivityRequest activity: firstLevelActivities) {
//			List<RetrieveActivityRequest> secondLevelActivities = this.getSecondLevelActivity(activity.getId());
//			allActivities.put(activity, secondLevelActivities);
//		}
//		return allActivities;
//	}
	
	public List<ValueLabelChildren> getAllActivities() {
		List<ValueLabelChildren> allActivities = new ArrayList<>();
		List<RetrieveActivityRequest> firstLevelActivities = this.getFirstLevelActivity();
		for (RetrieveActivityRequest activity: firstLevelActivities) {
			ValueLabelChildren firstActivity = new ValueLabelChildren();
			firstActivity.setValue(activity.getId());
			firstActivity.setLabel(activity.getName());
			firstActivity.setChildren(this.getSecondLevelActivity(activity.getId()));
			allActivities.add(firstActivity);
		}
		return allActivities;
	}
}
