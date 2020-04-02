package pretty.april.achieveitserver.serivce;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import pretty.april.achieveitserver.entity.SkillDict;
import pretty.april.achieveitserver.service.SkillDictService;

@RunWith(SpringRunner.class)
@SpringBootTest
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
}
