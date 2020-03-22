package pretty.april.achieveitserver.serivce;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import pretty.april.achieveitserver.service.BusinessAreaDictService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BusinessAreaDictServiceTest {

	@Autowired
	private BusinessAreaDictService businessAreaDictService;
	
	@Test
	public void getBusinessAreaIdByBusinessAreaNameTest() {
		String businessAreaName = "area1";
		Integer id = businessAreaDictService.getBusinessAreaIdByBusinessAreaName(businessAreaName);
		System.out.println("id = "+id);
	}
}
