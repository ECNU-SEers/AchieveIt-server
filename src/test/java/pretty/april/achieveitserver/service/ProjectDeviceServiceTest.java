package pretty.april.achieveitserver.service;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import pretty.april.achieveitserver.dto.PageDTO;
import pretty.april.achieveitserver.request.device.CreateOrUpdateProjectDeviceRequest;
import pretty.april.achieveitserver.request.device.RetrieveDeviceInspectionRequest;
import pretty.april.achieveitserver.request.device.ShowProjectDeviceListRequest;
import pretty.april.achieveitserver.service.ProjectDeviceService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectDeviceServiceTest {

	@Autowired
	private ProjectDeviceService projectDeviceService;
	
	@Test
	public void createProjectDevice() {
		CreateOrUpdateProjectDeviceRequest request = new CreateOrUpdateProjectDeviceRequest();
		request.setOuterId("device001");
		request.setType("PAD");
		request.setProjectId(3);
		request.setManagerId(1);
		request.setManagerName("admin");
		request.setStartDate(LocalDate.now());
		request.setDueDate(LocalDate.now().plusDays(50));
		projectDeviceService.createProjectDevice(request);
	}
	
	@Test
	public void createProjectDevice_inner_exception() {
		CreateOrUpdateProjectDeviceRequest request = new CreateOrUpdateProjectDeviceRequest();
		request.setOuterId("1");
		request.setType("PAD");
		request.setProjectId(1);
		request.setManagerId(1);
		request.setManagerName("admin");
		request.setStartDate(LocalDate.now());
		request.setDueDate(LocalDate.now().plusDays(50));
		projectDeviceService.createProjectDevice(request);
	}
	
	@Test
	public void createProjectDevice_outer_exception() {
		CreateOrUpdateProjectDeviceRequest request = new CreateOrUpdateProjectDeviceRequest();
		request.setOuterId("1");
		request.setType("PAD");
		request.setProjectId(3);
		request.setManagerId(1);
		request.setManagerName("admin");
		request.setStartDate(LocalDate.now());
		request.setDueDate(LocalDate.now().plusDays(50));
		projectDeviceService.createProjectDevice(request);
	}
	
	@Test
	public void searchProjectDeviceWithOuterIdIncludingKeywordTest() {
		String keyword = "1";
		Integer projectId = 1;
		List<String> allIds = projectDeviceService.searchProjectDeviceWithOuterIdIncludingKeyword(keyword, projectId);
		System.out.println(allIds.size());
		assertEquals(2, allIds.size());
	}
	
	@Test
	public void showProjectDeviceListTest() {
		String outerId = "1";
		Integer projectId = 1;
		ShowProjectDeviceListRequest request = new ShowProjectDeviceListRequest();
		request = projectDeviceService.showProjectDeviceList(outerId, projectId);
		System.out.println("outerId = "+request.getOuterId());
		System.out.println("type = "+request.getType());
		System.out.println("managerId = "+request.getManagerId());
		System.out.println("managerName = "+request.getManagerName());
		System.out.println("startDate = "+request.getStartDate());
		System.out.println("dueDate = "+request.getDueDate());
		System.out.println("state = "+request.getState());
		System.out.println("returnDate = "+request.getReturnDate());
	}
	
	@Test
	public void showDevicesTest() {
		Integer projectId = new Integer(1);
		String keyword = "1";
		PageDTO<ShowProjectDeviceListRequest> page = projectDeviceService.showDevices(1, 10, projectId, keyword);
		System.out.println(page.getTotal());
		for (ShowProjectDeviceListRequest request: page.getItems()) {
			System.out.println("outerId = "+request.getOuterId());
			System.out.println("type = "+request.getType());
			System.out.println("managerId = "+request.getManagerId());
			System.out.println("managerName = "+request.getManagerName());
			System.out.println("startDate = "+request.getStartDate());
			System.out.println("endDate = "+request.getDueDate());
			System.out.println("returnDate = "+request.getReturnDate());
			System.out.println("-------------------------------------");
		}
	}
	
	@Test
	public void retrieveDeviceInspectionTest() {
		String deviceOuterId = "1";
		Integer projectId = 1;
		List<RetrieveDeviceInspectionRequest> request = projectDeviceService.retrieveDeviceInspection(deviceOuterId, projectId);
		System.out.println("size = "+request.size());
		for (RetrieveDeviceInspectionRequest info: request) {
			System.out.println("inspectDate = "+info.getInspectDate());
			System.out.println("intact = "+info.getIntact());
			System.out.println("remark = "+info.getRemark());
			System.out.println("-------------------------------------");
		}
		assertEquals(5, request.size());
	}
	
	@Test
	public void retrieveDeviceInspectionByPageTest() {
		String deviceOuterId = "1";
		Integer projectId = 1;
		PageDTO<RetrieveDeviceInspectionRequest> request = projectDeviceService.retrieveDeviceInspectionByPage(1, 10, deviceOuterId, projectId);
		System.out.println(request.getTotal());
		for (RetrieveDeviceInspectionRequest info: request.getItems()) {
			System.out.println("inspectDate = "+info.getInspectDate());
			System.out.println("intact = "+info.getIntact());
			System.out.println("remark = "+info.getRemark());
			System.out.println("-------------------------------------");
		}
	}
	
	@Test
	public void updateProjectDevice() {
		CreateOrUpdateProjectDeviceRequest request = new CreateOrUpdateProjectDeviceRequest();
		request.setOuterId("2");
		request.setType("电脑");
		request.setProjectId(1);
		request.setManagerId(1);
		request.setManagerName("admin");
		request.setStartDate(LocalDate.now().minusDays(4));
		request.setDueDate(LocalDate.now().plusDays(40));
		projectDeviceService.updateProjectDeviceInfo(request);
	}
	
	@Test
	public void returnProjectDevice() {
		String outerId = "2";
		Integer projectId = new Integer(1);
		projectDeviceService.returnProjectDevice(outerId, projectId);
	}
	
	@Test
	public void returnProjectDevice_exception() {
		String outerId = "3";
		Integer projectId = new Integer(1);
		projectDeviceService.returnProjectDevice(outerId, projectId);
	}
}
