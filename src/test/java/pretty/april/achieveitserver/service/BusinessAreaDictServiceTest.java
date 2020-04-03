package pretty.april.achieveitserver.service;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import pretty.april.achieveitserver.entity.BusinessAreaDict;
import pretty.april.achieveitserver.service.BusinessAreaDictService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BusinessAreaDictServiceTest {

	@Autowired
	private BusinessAreaDictService businessAreaDictService;
	
	@Test
	public void getBusinessAreaIdByBusinessAreaNameTest() {
		String businessAreaName = "AI";
		Integer id = businessAreaDictService.getBusinessAreaIdByBusinessAreaName(businessAreaName);
		assertNotNull(id);
	}
	
	@Test
	public void getAllBusinessAreasTest() {
		List<BusinessAreaDict> allBusinessAreas = businessAreaDictService.getAllBusinessAreas();
		assertEquals(3, allBusinessAreas.size());
	}
}
