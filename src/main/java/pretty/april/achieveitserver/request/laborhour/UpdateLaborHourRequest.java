package pretty.april.achieveitserver.request.laborhour;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UpdateLaborHourRequest {
	
	private Integer id;
	
//	private LocalDate date;
	private Long date;
	private Integer functionId;
	
	private String functionName;
	
	private Integer subfunctionId;
	
	private String subfunctionName;
	
	private Integer activityId;
	
	private String activityName;
	
	private Integer subactivityId;
	
	private String subactivityName;

//    private LocalTime startTime;
	private Long startTime;
//    private LocalTime endTime;
	private Long endTime;
}
