package pretty.april.achieveitserver.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import pretty.april.achieveitserver.entity.SkillDict;

@Mapper
public interface SkillDictMapper extends BaseMapper<SkillDict> {

	@Select("SELECT * FROM skill_dict")
	List<SkillDict> selectAllSkillDict();
}
