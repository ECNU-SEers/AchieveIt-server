package pretty.april.achieveitserver.serivce;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import pretty.april.achieveitserver.dto.PageDTO;
import pretty.april.achieveitserver.entity.LaborHour;
import pretty.april.achieveitserver.request.laborhour.CreateLaborHourRequest;
import pretty.april.achieveitserver.request.laborhour.RetrieveLaborHourRequest;
import pretty.april.achieveitserver.request.laborhour.ShowLaborHourListRequest;
import pretty.april.achieveitserver.request.laborhour.ShowSubordinateLaborHourListRequest;
import pretty.april.achieveitserver.request.laborhour.UpdateLaborHourRequest;
import pretty.april.achieveitserver.service.LaborHourService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LaborHourServiceTest {

	@Autowired
	private LaborHourService laborHourService;
	
	@Test
	public void retrieveLaborHourByDatesTest() {
		LocalDate date1 = LocalDate.of(2020, 3, 22);
		LocalDate date2 = LocalDate.of(2020, 3, 27);
		Long startDateTimestamp = date1.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
		Long endDateTimestamp = date2.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
		PageDTO<RetrieveLaborHourRequest> request = laborHourService.retrieveLaborHourByDates(1, 10, startDateTimestamp, endDateTimestamp);
		System.out.println("size = "+request.getTotal());
		for (RetrieveLaborHourRequest r: request.getItems()) {
			System.out.println("id = "+r.getId());
			System.out.println("functionName = "+r.getFunctionName());
			System.out.println("activityName = "+r.getActivityName());
			System.out.println("state = "+r.getState());
			System.out.println("---------------------------------------------");
		}
	}
	
//	@Test
//	public void createLaborHourTest() throws Exception {
//		CreateLaborHourRequest request = new CreateLaborHourRequest();
////		request.setDate(LocalDate.of(2020, 03, 15));
//		request.setDate(LocalDate.of(2020, 03, 23));
//		request.setFunctionId(1);
//		request.setFunctionName("func1");
//		request.setSubfunctionId(2);
//		request.setSubfunctionName("func1.1");
//		request.setActivityId(1);
//		request.setActivityName("act1");
//		request.setSubactivityId(2);
//		request.setSubactivityName("act2");
//		request.setStartTime(LocalTime.now());
//		request.setEndTime(LocalTime.now().plusHours(2));
//		laborHourService.createLaborHour(request);
//	}
//	
//	@Test
//	public void showListTest() {
//		PageDTO<ShowLaborHourListRequest> request = laborHourService.showList(1, 10);
//		System.out.println("size = "+request.getTotal());
//		for (ShowLaborHourListRequest r: request.getItems()) {
//			System.out.println("id = "+r.getId());
//			System.out.println("functionName = "+r.getFunctionName());
//			System.out.println("subfunctionName = "+r.getSubfunctionName());
//			System.out.println("activityName = "+r.getActivityName());
//			System.out.println("subactivityName = "+r.getSubactivityName());
//			System.out.println("state = "+r.getState());
//			System.out.println("---------------------------------------------");
//		}
//	}
//	
//	@Test
//	public void updateLaborHourTest() throws Exception {
//		UpdateLaborHourRequest request = new UpdateLaborHourRequest();
//		request.setId(4);
//		request.setDate(LocalDate.of(2020, 03, 23));
//		request.setFunctionId(1);
//		request.setFunctionName("func1");
//		request.setSubfunctionId(2);
//		request.setSubfunctionName("func1.1");
//		request.setActivityId(1);
//		request.setActivityName("act1");
//		request.setSubactivityId(2);
//		request.setSubactivityName("act2");
//		request.setStartTime(LocalTime.now());
//		request.setEndTime(LocalTime.now().plusMinutes(55));
//		LaborHour lh = laborHourService.updateLaborHour(request);
//		System.out.println(lh.getStartTime());
//		System.out.println(lh.getEndTime());
//		System.out.println(lh.getSubmissionDate());
//	}
//	
//	@Test
//	public void retrieveLaborHourOfSubordinateTest() {
//		LocalDate date1 = LocalDate.of(2020, 3, 22);
//		LocalDate date2 = LocalDate.of(2020, 3, 27);
//		Long startDateTimestamp = date1.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
//		Long endDateTimestamp = date2.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
//		PageDTO<RetrieveLaborHourRequest> request = laborHourService.retrieveLaborHourOfSubordinate(1, 10, startDateTimestamp, endDateTimestamp);
//		System.out.println("size = "+request.getTotal());
//		for (RetrieveLaborHourRequest r: request.getItems()) {
//			System.out.println("id = "+r.getId());
//			System.out.println("functionName = "+r.getFunctionName());
//			System.out.println("activityName = "+r.getActivityName());
//			System.out.println("state = "+r.getState());
//			System.out.println("---------------------------------------------");
//		}
//	}
//	
//	@Test
//	public void showSubordinateListsTest() {
//		PageDTO<ShowSubordinateLaborHourListRequest> request = laborHourService.showSubordinateLists(1, 10);
//		System.out.println("size = "+request.getTotal());
//		for (ShowSubordinateLaborHourListRequest r: request.getItems()) {
//			System.out.println("id = "+r.getId());
//			System.out.println("functionName = "+r.getFunctionName());
//			System.out.println("subfunctionName = "+r.getSubfunctionName());
//			System.out.println("activityName = "+r.getActivityName());
//			System.out.println("subactivityName = "+r.getSubactivityName());
//			System.out.println("state = "+r.getState());
//			System.out.println("---------------------------------------------");
//		}
//	}
	
	
}
