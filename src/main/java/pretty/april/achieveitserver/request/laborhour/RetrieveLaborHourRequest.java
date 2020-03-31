package pretty.april.achieveitserver.request.laborhour;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RetrieveLaborHourRequest {

	private Integer id;
	
	private Long date;
	
	private String functionName;
	
	private String activityName;

    private Long startTime;

    private Long endTime;

    private Long submissionDate;
    
    private String state;
}
