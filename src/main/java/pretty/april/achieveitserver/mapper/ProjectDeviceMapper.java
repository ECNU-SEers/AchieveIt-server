package pretty.april.achieveitserver.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import pretty.april.achieveitserver.entity.Project;
import pretty.april.achieveitserver.entity.ProjectDevice;

@Mapper
public interface ProjectDeviceMapper extends BaseMapper<ProjectDevice> {

	/**
	 * 利用设备ID和项目表ID查询设备数
	 * @param outerId
	 * @return 设备数
	 */
	@Select("SELECT COUNT(*) FROM project_device WHERE outer_id = #{outerId} AND project_id = #{projectId}")
	int selectCountByOuterIdAndProjectId(@Param("outerId")String outerId, @Param("projectId")Integer projectId);
	
	/**
	 * 利用设备ID查看该设备的所有历史状态
	 * @param outerId
	 * @return 所有状态
	 */
	@Select("SELECT state FROM project_device WHERE outer_id = #{outerId}")
	List<String> selectStatesByOuterId(String outerId);
	
	/**
	 * 查询设备ID中包含关键字的所有设备
	 * @param keyword
	 * @return
	 */
	@Select("SELECT outer_id FROM project_device WHERE outer_id LIKE \"%\"#{keyword}\"%\" AND project_id = #{projectId}")
	List<String> selectByOuterIdLikeKeyword(@Param("keyword")String keyword, @Param("projectId")Integer projectId);
}
