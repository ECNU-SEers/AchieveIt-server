package pretty.april.achieveitserver.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import pretty.april.achieveitserver.service.ProjectSkillService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
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
