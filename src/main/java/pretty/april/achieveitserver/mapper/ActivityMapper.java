package pretty.april.achieveitserver.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import pretty.april.achieveitserver.entity.Activity;
import pretty.april.achieveitserver.request.activity.RetrieveActivityRequest;

@Mapper
public interface ActivityMapper extends BaseMapper<Activity> {

	/**
	 * 查询所有一级活动
	 * @return
	 */
	@Select("SELECT id, name FROM activity WHERE parent_id is null")
	List<RetrieveActivityRequest> selectAllFirstLevelActivities();
	
	/**
	 * 查询某个一级活动的所有二级活动
	 * @param parentId
	 * @return
	 */
	@Select("SELECT id, name FROM activity WHERE parent_id = #{parentId}")
	List<RetrieveActivityRequest> selectAllSecondLevelActivities(Integer parentId);
}
