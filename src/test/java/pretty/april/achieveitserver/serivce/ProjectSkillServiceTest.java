package pretty.april.achieveitserver.serivce;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import pretty.april.achieveitserver.service.ProjectSkillService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectSkillServiceTest {

	@Autowired
	private ProjectSkillService projectSkillService;
	
	@Test
	public void getProjectSkillByProjectId() {
		Integer projectId = new Integer(1);
		assertNotNull(projectSkillService.getProjectSkillByProjectId(projectId));
	}
	
	@Test
	public void deleteProjectSkillByProjectIdTest() {
		Integer projectId = new Integer(2);
		assertEquals(0, projectSkillService.deleteProjectSkillByProjectId(projectId));
	}
}