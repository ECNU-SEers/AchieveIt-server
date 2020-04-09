package pretty.april.achieveitserver.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import pretty.april.achieveitserver.entity.BusinessAreaDict;

@Mapper
public interface BusinessAreaDictMapper extends BaseMapper<BusinessAreaDict> {

	@Select("SELECT * FROM business_area_dict")
	List<BusinessAreaDict> selectAllBusinessAreaDict();
	
	@Insert("<script>" +
            "insert into business_area_dict (name) values" +
            "<foreach collection='businessAreas' item='businessArea' open='' separator=',' close='' >" +
            "(#{businessArea})" +
            "</foreach>" +
            "</script>")
	int insertBatch(@Param("businessAreas") List<String> businessAreas);
}
