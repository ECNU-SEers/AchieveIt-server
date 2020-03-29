package pretty.april.achieveitserver.serivce;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import pretty.april.achieveitserver.request.activity.RetrieveActivityRequest;
import pretty.april.achieveitserver.service.ActivityService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityServiceTest {

	@Autowired
	private ActivityService activityService;
	
	@Test
	public void getBusinessAreaIdByBusinessAreaNameTest() { 
		Map<RetrieveActivityRequest, List<RetrieveActivityRequest>> allActivities = activityService.getAllActivities();
		for (RetrieveActivityRequest key: allActivities.keySet()) {
			System.out.println("1st id = "+key.getId()+"  name = "+key.getName());
			List<RetrieveActivityRequest> secondActivities = allActivities.get(key);
			for (RetrieveActivityRequest r: secondActivities) {
				System.out.println(r.getId()+"   "+r.getName());
			}
			System.out.println("==================");
		}
		System.out.println(activityService.getAllActivities());
	}
}
