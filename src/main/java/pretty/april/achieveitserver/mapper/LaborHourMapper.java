package pretty.april.achieveitserver.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import pretty.april.achieveitserver.entity.LaborHour;

@Mapper
public interface LaborHourMapper extends BaseMapper<LaborHour> {

	/**
	 * 选择某个领导的所有项目下级的工时记录
	 * @param userId 领导ID
	 * @return
	 */
	@Select("SELECT labor_hour.* FROM labor_hour,(SELECT project_id, user_id FROM project_member WHERE leader_id = #{userId}) a WHERE labor_hour.date > #{startDate} AND labor_hour.date < #{endDate} AND a.project_id = labor_hour.project_id AND a.user_id = labor_hour.user_id")
	List<LaborHour> selectByLeaderId(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("userId") Integer userId, Page<LaborHour> page);
}
