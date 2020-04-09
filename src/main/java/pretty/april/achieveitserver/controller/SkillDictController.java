package pretty.april.achieveitserver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pretty.april.achieveitserver.dto.Response;
import pretty.april.achieveitserver.entity.SkillDict;
import pretty.april.achieveitserver.service.SkillDictService;
import pretty.april.achieveitserver.utils.ResponseUtils;

@RestController
@RequestMapping("/api/skill")
public class SkillDictController {
	
	@Autowired
	private SkillDictService skillDictService;
	
	/**
	 * 所有的项目技术
	 * @return
	 */
	@GetMapping("/all")
	public Response<List<SkillDict>> getAllSkills() {
		return ResponseUtils.successResponse(skillDictService.getAllSkills());
	}
	
	/**
	 * 增加技术
	 * @param skillName
	 * @return
	 */
	@PostMapping("/add")
	public Response<?> addSkill(@RequestParam(value="skillName") String skillName) {
		skillDictService.insertSkill(skillName);
		return ResponseUtils.successResponse();
	}
	
	/**
	 * 更新技术
	 * @param skillId
	 * @param newSkillName
	 * @return
	 */
	@PutMapping("/update")
	public Response<?> updateSkill(@RequestParam(value="skillId") Integer skillId, @RequestParam(value="newSkillName") String newSkillName) {
		skillDictService.updateSkill(skillId, newSkillName);
		return ResponseUtils.successResponse();
	}
	
	/**
	 * 删除技术
	 * @param skillIds
	 * @return
	 */
	@DeleteMapping("/delete")
	public Response<?> deleteSkill(@RequestParam(value="skillIds") List<Integer> skillIds) {
		skillDictService.deleteSkills(skillIds);
		return ResponseUtils.successResponse();
	}
}
