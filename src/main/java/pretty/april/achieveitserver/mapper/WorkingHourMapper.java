package pretty.april.achieveitserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import pretty.april.achieveitserver.entity.LaborHour;
import pretty.april.achieveitserver.model.WorkingHour;

import java.util.List;

@Mapper
public interface WorkingHourMapper extends BaseMapper<LaborHour> {

    @Select("SELECT COALESCE(SUM(TIME_TO_SEC(end_time)-TIME_TO_SEC(start_time)),0) FROM labor_hour WHERE state = '已通过' AND user_id = #{userId} AND project_id = #{projectId}")
    Float selectWorkingHour(Integer userId, Integer projectId);
}
