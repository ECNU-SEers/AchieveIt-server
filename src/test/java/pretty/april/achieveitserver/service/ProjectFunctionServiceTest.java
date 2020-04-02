package pretty.april.achieveitserver.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import pretty.april.achieveitserver.entity.ProjectFunction;
import pretty.april.achieveitserver.service.ProjectFunctionService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectFunctionServiceTest {

	@Autowired
	private ProjectFunctionService projectFunctionService;
	
	@Test
	public void getProjectFunctionByProjectIdTest() {
		Integer projectId = new Integer(1);
		List<ProjectFunction> projectFunctions = projectFunctionService.getProjectFunctionByProjectId(projectId);
		assertEquals(4, projectFunctions.size());
	}
}
