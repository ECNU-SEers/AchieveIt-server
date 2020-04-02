package pretty.april.achieveitserver.service;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import pretty.april.achieveitserver.service.ProjectIdService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectIdServiceTest {

	@Autowired
	private ProjectIdService projectIdService;
	
	@Test
	public void getAllProjectIdsTest() {
		List<String> projectIds = projectIdService.getAllProjectIds();
		for (String projectId: projectIds) {
			System.out.println("id = "+ projectId);
		}
		assertEquals(100,projectIds.size());
	}
	
}
