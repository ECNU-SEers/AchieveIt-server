package pretty.april.achieveitserver.serivce;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import pretty.april.achieveitserver.entity.Milestone;
import pretty.april.achieveitserver.service.MilestoneService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MilestoneServiceTest {

	@Autowired
	private MilestoneService milestoneService;
	
	@Test
	public void getProjectMilestoneByProjectIdTest() {
		Integer projectId = new Integer(1);
		List<Milestone> allMilestones = milestoneService.getProjectMilestoneByProjectId(projectId);
		assertEquals(2, allMilestones.size());
	}
}
