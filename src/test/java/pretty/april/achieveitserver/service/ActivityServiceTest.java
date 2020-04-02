package pretty.april.achieveitserver.service;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import pretty.april.achieveitserver.request.activity.RetrieveActivityRequest;
import pretty.april.achieveitserver.request.activity.ValueLabelChildren;
import pretty.april.achieveitserver.service.ActivityService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityServiceTest {

	@Autowired
	private ActivityService activityService;
	
	@Test
	public void selectByIdTest() {
		Integer id = new Integer(1);
		assertNotNull(activityService.getById(id));
	}
	
	@Test
	public void getAllActivitiesTest() { 
		List<ValueLabelChildren> allActivities = activityService.getAllActivities();
		for (ValueLabelChildren key: allActivities) {
			System.out.println("1st id = "+key.getId()+"  name = "+key.getName());
			List<RetrieveActivityRequest> secondActivities = key.getChildren();
			for (RetrieveActivityRequest r: secondActivities) {
				System.out.println(r.getId()+"   "+r.getName());
			}
			System.out.println("==================");
		}
		assertEquals(2, allActivities.size());
	}
}
