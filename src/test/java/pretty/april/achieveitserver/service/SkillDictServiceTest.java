package pretty.april.achieveitserver.service;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import pretty.april.achieveitserver.entity.SkillDict;
import pretty.april.achieveitserver.request.dict.AddSkillDictRequest;
import pretty.april.achieveitserver.request.dict.UpdateSkillDictRequest;
import pretty.april.achieveitserver.service.SkillDictService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class SkillDictServiceTest {

	@Autowired
	private SkillDictService skillDictService;
	
	@Test
	public void getBusinessAreaIdByBusinessAreaNameTest() {
		String skillName = "Java";
		Integer id = skillDictService.getSkillIdBySkillName(skillName);
		assertNotNull(id);
	}
	
	@Test
	public void getAllBusinessAreasTest() {
		List<SkillDict> allSkills = skillDictService.getAllSkills();
		assertEquals(4, allSkills.size());
	}
	
	@Test
	public void insertSkillTest() {
		AddSkillDictRequest request = new AddSkillDictRequest();
		request.setSkillName("Mybatis");
		System.out.println(skillDictService.insertSkill(request));
	}
	
	@Test
	public void updateSkillTest() {
		UpdateSkillDictRequest request = new UpdateSkillDictRequest();
		request.setSkillId(new Integer(1));
		request.setNewSkillName("Hibernate");
		System.out.println(skillDictService.updateSkill(request));
	}
	
	@Test
	public void deleteSkills() {
		List<Integer> skillIds = new ArrayList<>();
		skillIds.add(1);
		skillIds.add(2);
		System.out.println(skillDictService.deleteSkills(skillIds));
	}
}
