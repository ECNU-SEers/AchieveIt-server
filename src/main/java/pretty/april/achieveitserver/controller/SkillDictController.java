package pretty.april.achieveitserver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pretty.april.achieveitserver.dto.Response;
import pretty.april.achieveitserver.entity.SkillDict;
import pretty.april.achieveitserver.service.SkillDictService;
import pretty.april.achieveitserver.utils.ResponseUtils;

@RestController
@RequestMapping("/api/skillDict")
public class SkillDictController {
	
	@Autowired
	private SkillDictService skillDictService;
	
	@GetMapping("/allSkills")
	public Response<List<SkillDict>> getAllSkills() {
		return ResponseUtils.successResponse(skillDictService.getAllSkills());
	}
	

}
