package pretty.april.achieveitserver.serivce;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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
		LocalDate date2 = LocalDate.of(2020, 3, 30);
		Long startDateTimestamp = date1.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
		Long endDateTimestamp = date2.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
		PageDTO<RetrieveLaborHourRequest> request = laborHourService.retrieveLaborHourByDates(1, 10, startDateTimestamp, endDateTimestamp);
		System.out.println("size = "+request.getTotal());
		for (RetrieveLaborHourRequest r: request.getItems()) {
			System.out.println("id = "+r.getId());
			System.out.println("date = "+r.getDate());
			System.out.println("functionName = "+r.getFunctionName());
			System.out.println("activityName = "+r.getActivityName());
			System.out.println("startTime = "+r.getStartTime());
			System.out.println("endTime = "+r.getEndTime());
			System.out.println("state = "+r.getState());
			System.out.println("---------------------------------------------");
		}
	}
	
	@Test
	public void createLaborHourTest() throws Exception {
		CreateLaborHourRequest request = new CreateLaborHourRequest();
		request.setDate(1085587265378L);
		request.setFunctionId(3);
		request.setFunctionName("func1");
		request.setSubfunctionId(4);
		request.setSubfunctionName("func1.1");
		request.setActivityId(1);
		request.setActivityName("act1");
		request.setSubactivityId(3);
		request.setSubactivityName("act3");
		request.setStartTime(1585587265378L);
		request.setEndTime(  1585588265378L);
		laborHourService.createLaborHour(request);
	}
	
	@Test
	public void showListTest() {
		PageDTO<ShowLaborHourListRequest> request = laborHourService.showList(1, 10);
		System.out.println("size = "+request.getTotal());
		for (ShowLaborHourListRequest r: request.getItems()) {
			System.out.println("id = "+r.getId());
			System.out.println("functionName = "+r.getFunctionName());
			System.out.println("subfunctionName = "+r.getSubfunctionName());
			System.out.println("activityName = "+r.getActivityName());
			System.out.println("subactivityName = "+r.getSubactivityName());
			System.out.println("startTime = "+r.getStartTime());
			System.out.println("state = "+r.getState());
			System.out.println("---------------------------------------------");
		}
	}
	
	@Test
	public void updateLaborHourTest() throws Exception {
		UpdateLaborHourRequest request = new UpdateLaborHourRequest();
		request.setId(4);
		request.setDate(1585587265378L);
		request.setFunctionId(3);
		request.setFunctionName("func1");
		request.setSubfunctionId(4);
		request.setSubfunctionName("func1.1");
		request.setActivityId(1);
		request.setActivityName("act1");
		request.setSubactivityId(3);
		request.setSubactivityName("act3");
		request.setStartTime(1585587265378L);
		request.setEndTime(1585587537813L);
		String result = laborHourService.updateLaborHour(request);
		System.out.println(result);
		System.out.println("date = "+request.getDate());
	}
	
	@Test
	public void retrieveLaborHourOfSubordinateTest() {
		LocalDate date1 = LocalDate.of(2020, 3, 20);
		LocalDate date2 = LocalDate.of(2020, 3, 27);
		Long startDateTimestamp = date1.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
		Long endDateTimestamp = date2.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
		PageDTO<RetrieveLaborHourRequest> request = laborHourService.retrieveLaborHourOfSubordinate(1, 10, startDateTimestamp, endDateTimestamp);
		System.out.println("size = "+request.getTotal());
		for (RetrieveLaborHourRequest r: request.getItems()) {
			System.out.println("id = "+r.getId());
			System.out.println("functionName = "+r.getFunctionName());
			System.out.println("activityName = "+r.getActivityName());
			System.out.println("date = "+r.getDate());
			System.out.println("state = "+r.getState());
			System.out.println("---------------------------------------------");
		}
	}
	
	@Test
	public void showSubordinateListsTest() {
		PageDTO<ShowSubordinateLaborHourListRequest> request = laborHourService.showSubordinateLists(1, 10);
		System.out.println("size = "+request.getTotal());
		for (ShowSubordinateLaborHourListRequest r: request.getItems()) {
			System.out.println("id = "+r.getId());
			System.out.println("functionName = "+r.getFunctionName());
			System.out.println("subfunctionName = "+r.getSubfunctionName());
			System.out.println("activityName = "+r.getActivityName());
			System.out.println("subactivityName = "+r.getSubactivityName());
			System.out.println("submissionDate = "+r.getSubmissionDate());
			System.out.println("state = "+r.getState());
			System.out.println("---------------------------------------------");
		}
	}
	
	@Test
	public void acceptLaborHourInfoTest() {
		Integer id = 1;
		System.out.println(laborHourService.acceptLaborHourInfo(id));
	}
	
	@Test
	public void returnLaborHourInfoTest() {
		Integer id = 1;
		System.out.println(laborHourService.returnLaborHourInfo(id));
	}
	
}
