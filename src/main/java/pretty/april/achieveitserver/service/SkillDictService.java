package pretty.april.achieveitserver.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import pretty.april.achieveitserver.entity.SkillDict;
import pretty.april.achieveitserver.mapper.SkillDictMapper;
import pretty.april.achieveitserver.request.dict.AddSkillDictRequest;
import pretty.april.achieveitserver.request.dict.UpdateSkillDictRequest;

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
	
	/**
	 * 增加技术
	 * @param request
	 * @return
	 */
	public int insertSkill(AddSkillDictRequest request) {
		String skillName = request.getSkillName();
		SkillDict skillDict = new SkillDict();
		skillDict.setName(skillName);
		return this.baseMapper.insert(skillDict);
	}
	
	/**
	 * 更新技术
	 * @param request
	 * @return
	 */
	public int updateSkill(UpdateSkillDictRequest request) {
		Integer id = request.getSkillId();
		String newSkillName = request.getNewSkillName();
		SkillDict skill = this.baseMapper.selectById(id);
		skill.setName(newSkillName);
		return this.baseMapper.updateById(skill);
	}
	
	/**
	 * 删除技术
	 * @param skills
	 * @return
	 */
	public int deleteSkills(List<Integer> skillIds) {
		return this.baseMapper.deleteBatchIds(skillIds);
	}
}
