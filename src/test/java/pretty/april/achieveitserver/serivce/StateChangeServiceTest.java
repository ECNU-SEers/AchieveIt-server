package pretty.april.achieveitserver.serivce;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import pretty.april.achieveitserver.request.statechange.RetrieveStateChangeRequest;
import pretty.april.achieveitserver.service.StateChangeService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StateChangeServiceTest {

	@Autowired
	private StateChangeService stateChangeService;
	
	@Test
	public void showStateChangeListTest() {
		String outerId = "2020-ECNU-D-00";
		List<RetrieveStateChangeRequest> request = stateChangeService.showStateChangeList(outerId);
		System.out.println(request.size());
		
		for (RetrieveStateChangeRequest r: request) {
			System.out.println("projectId = "+r.getProjectId());
			System.out.println("formerState = "+r.getFormerState());
			System.out.println("latterState = "+r.getLatterState());
			System.out.println("operatorId = "+r.getOperatorId());
			System.out.println("username = "+r.getUsername());
			System.out.println("realName = "+r.getRealName());
			System.out.println("operation = "+r.getOperation());
			System.out.println("changeDate = "+r.getChangeDate());
			System.out.println("remark = "+r.getRemark());
			System.out.println("----------------------------------");
		}
	}
}
