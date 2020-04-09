package pretty.april.achieveitserver.service;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import pretty.april.achieveitserver.entity.BusinessAreaDict;
import pretty.april.achieveitserver.service.BusinessAreaDictService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
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
	
	@Test
	public void addBusinessAreasTest() {
		List<String> businessAreas = new ArrayList<>();
		businessAreas.add("finance");
		businessAreas.add("insurance");
		businessAreas.add("actuary");
		System.out.println(businessAreaDictService.addBusinessAreas(businessAreas));
	}
	
	@Test
	public void insertBusinessAreaTest() {
		String businessArea = "architect";
		System.out.println(businessAreaDictService.insertBusinessArea(businessArea));
	}
	
	@Test
	public void updateBusinessAreaTest() {
		Integer id = new Integer(3);
		String newBusinessAreaName = "math";
		System.out.println(businessAreaDictService.updateBusinessArea(id, newBusinessAreaName));
	}
	
	@Test
	public void deleteBusinessAreasTest() {
		List<Integer> businessAreaIds = new ArrayList<>();
		businessAreaIds.add(5);
		businessAreaIds.add(6);
		System.out.println(businessAreaDictService.deleteBusinessAreas(businessAreaIds));
	}
}
