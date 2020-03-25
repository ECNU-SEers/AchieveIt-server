package pretty.april.achieveitserver.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import pretty.april.achieveitserver.entity.Activity;
import pretty.april.achieveitserver.mapper.ActivityMapper;

@Service
public class ActivityService extends ServiceImpl<ActivityMapper, Activity> {

	public Activity getById(Integer id) {
		return this.baseMapper.selectById(id);
	}
}
