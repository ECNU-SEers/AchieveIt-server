package pretty.april.achieveitserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import pretty.april.achieveitserver.entity.DeviceInspection;
import pretty.april.achieveitserver.request.device.RetrieveDeviceInspectionRequest;

@Mapper
public interface DeviceInspectionMapper extends BaseMapper<DeviceInspection> {
	
	/**
	 * 查询设备ID为$deviceId的设备的所有检查信息
	 * @param deviceId
	 * @return
	 */
	@Select("SELECT * FROM device_inspection WHERE device_id = #{deviceId} ORDER BY inspect_date")
	List<DeviceInspection> selectInfoByDeviceId(Integer deviceId);
}
