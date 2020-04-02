package pretty.april.achieveitserver.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import pretty.april.achieveitserver.service.ProjectBusinessAreaService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectBusinessAreaServiceTest {

	@Autowired
	private ProjectBusinessAreaService projectBusinessAreaService;
	
	@Test
	public void getProjectBusinessAreaByProjectIdTest() {
		Integer projectId = new Integer(1);
		assertNotNull(projectBusinessAreaService.getProjectBusinessAreaByProjectId(projectId));
	}
	
	@Test
	public void deleteProjectBusinessAreaByProjectIdTest() {
		Integer projectId = new Integer(2);
		assertEquals(0, projectBusinessAreaService.deleteProjectBusinessAreaByProjectId(projectId));
	}
}
