package pretty.april.achieveitserver.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import pretty.april.achieveitserver.entity.BusinessAreaDict;

@Mapper
public interface BusinessAreaDictMapper extends BaseMapper<BusinessAreaDict> {

	@Select("SELECT * FROM business_area_dict")
	List<BusinessAreaDict> selectAllBusinessAreaDict();
}
