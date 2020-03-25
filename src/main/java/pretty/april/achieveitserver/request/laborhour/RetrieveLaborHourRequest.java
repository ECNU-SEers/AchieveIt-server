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
public class RetrieveLaborHourRequest {

	private LocalDate date;
	
	private String functionName;
	
	private String activityName;

    private LocalTime startTime;

    private LocalTime endTime;
    
    private LocalDateTime submissionDate;

    private String state;
}
