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
import pretty.april.achieveitserver.request.dict.AddBusinessAreaDictRequest;
import pretty.april.achieveitserver.request.dict.UpdateBusinessAreaDictRequest;
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
		AddBusinessAreaDictRequest request = new AddBusinessAreaDictRequest();
		request.setBusinessAreaName("architect");
		System.out.println(businessAreaDictService.insertBusinessArea(request));
	}
	
	@Test
	public void updateBusinessAreaTest() {
		UpdateBusinessAreaDictRequest request = new UpdateBusinessAreaDictRequest();
		request.setBusinessAreaId(new Integer(3));
		request.setNewBusinessAreaName("math");
		System.out.println(businessAreaDictService.updateBusinessArea(request));
	}
	
	@Test
	public void deleteBusinessAreasTest() {
		List<Integer> businessAreaIds = new ArrayList<>();
		businessAreaIds.add(5);
		businessAreaIds.add(6);
		System.out.println(businessAreaDictService.deleteBusinessAreas(businessAreaIds));
	}
}
