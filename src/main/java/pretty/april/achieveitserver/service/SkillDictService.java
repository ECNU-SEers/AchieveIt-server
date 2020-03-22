package pretty.april.achieveitserver.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import pretty.april.achieveitserver.entity.SkillDict;
import pretty.april.achieveitserver.mapper.SkillDictMapper;

@Service
public class SkillDictService extends ServiceImpl<SkillDictMapper, SkillDict> {

	/**
	 * 通过技术名称查找技术Id
	 * @param skillName 技术名称
	 * @return 该技术名称对应的技术Id
	 */
	public Integer getSkillIdBySkillName(String skillName) {
		QueryWrapper<SkillDict> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(SkillDict::getName, skillName);
		SkillDict skill = this.getOne(queryWrapper);
		return skill.getId();
	}
	
	/**
	 * 查询所有技术
	 * @return 所有技术
	 */
	public List<SkillDict> getAllSkills() {
		return this.baseMapper.selectAllSkillDict();
	}
}
