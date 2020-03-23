package pretty.april.achieveitserver.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
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
	@Select("SELECT COUNT(*) FROM project_device WHERE outer_id = #{outerId} and project_id = #{projectId}")
	int selectCountByOuterIdAndProjectId(String outerId, Integer projectId);
	
	/**
	 * 查询设备ID中包含关键字的所有设备
	 * @param keyword
	 * @return
	 */
	@Select("SELECT outer_id FROM project_device WHERE outer_id LIKE \"%\"#{keyword}\"%\"")
	List<String> selectByOuterIdLikeKeyword(String keyword);
}
