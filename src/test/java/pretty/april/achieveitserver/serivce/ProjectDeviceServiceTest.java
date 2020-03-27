package pretty.april.achieveitserver.serivce;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

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
	
//	@Test
//	public void createProjectDevice() throws Exception {
//		CreateOrUpdateProjectDeviceRequest request = new CreateOrUpdateProjectDeviceRequest();
//		request.setOuterId("device001");
//		request.setType("PAD");
//		request.setProjectId(3);
//		request.setManagerId(2);
//		request.setManagerName("Jack");
//		request.setStartDate(LocalDate.now(Clock.systemUTC()).minusDays(4));
//		request.setDueDate(LocalDate.now(Clock.systemUTC()).plusDays(50));
//		projectDeviceService.createProjectDevice(request);
//	}
//	
//	@Test
//	public void searchProjectDeviceWithOuterIdIncludingKeywordTest() {
//		String keyword = "de";
//		Integer projectId = 2;
//		List<String> allIds = projectDeviceService.searchProjectDeviceWithOuterIdIncludingKeyword(keyword, projectId);
//		System.out.println(allIds.size());
//	}
//	
//	@Test
//	public void showProjectDeviceListTest() {
//		String outerId = "device001";
//		Integer projectId = 2;
//		ShowProjectDeviceListRequest request = new ShowProjectDeviceListRequest();
//		request = projectDeviceService.showProjectDeviceList(outerId, projectId);
//		System.out.println("outerId = "+request.getOuterId());
//		System.out.println("type = "+request.getType());
//		System.out.println("managerId = "+request.getManagerId());
//		System.out.println("managerName = "+request.getManagerName());
//		System.out.println("startDate = "+request.getStartDate());
//		System.out.println("dueDate = "+request.getDueDate());
//		System.out.println("state = "+request.getState());
//		System.out.println("returnDate = "+request.getReturnDate());
//	}
	
	@Test
	public void showDevicesTest() {
		Integer projectId = 1;
		PageDTO<ShowProjectDeviceListRequest> page = projectDeviceService.showDevices(1, 3, projectId);
		System.out.println(page.getTotal());
	}
	
//	@Test
//	public void retrieveDeviceInspectionTest() {
//		Integer deviceId = 1;
//		List<RetrieveDeviceInspectionRequest> request = projectDeviceService.retrieveDeviceInspection(deviceId);
//		System.out.println("size = "+request.size());
//		for (RetrieveDeviceInspectionRequest info: request) {
//			System.out.println("inspectDate = "+info.getInspectDate());
//			System.out.println("intact = "+info.getIntact());
//			System.out.println("remark = "+info.getRemark());
//			System.out.println("-------------------------------------");
//		}
//	}
//	
//	@Test
//	public void retrieveDeviceInspectionByPageTest() {
//		Integer deviceId = 1;
//		PageDTO<RetrieveDeviceInspectionRequest> request = projectDeviceService.retrieveDeviceInspectionByPage(1, 10, deviceId);
//		System.out.println(request.getTotal());
//		for (RetrieveDeviceInspectionRequest info: request.getItems()) {
//			System.out.println("inspectDate = "+info.getInspectDate());
//			System.out.println("intact = "+info.getIntact());
//			System.out.println("remark = "+info.getRemark());
//			System.out.println("-------------------------------------");
//		}
//	}
//	
//	@Test
//	public void updateProjectDevice() throws Exception {
//		CreateOrUpdateProjectDeviceRequest request = new CreateOrUpdateProjectDeviceRequest();
//		request.setOuterId("device001");
//		request.setType("电脑");
//		request.setProjectId(2);
//		request.setManagerId(1);
//		request.setManagerName("admin");
//		request.setStartDate(LocalDate.now(Clock.systemUTC()).minusDays(4));
//		request.setDueDate(LocalDate.now(Clock.systemUTC()).plusDays(40));
//		projectDeviceService.updateProjectDeviceInfo(request);
//	}
}
