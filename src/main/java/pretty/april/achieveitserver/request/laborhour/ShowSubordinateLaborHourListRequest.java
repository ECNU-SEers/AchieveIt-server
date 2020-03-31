package pretty.april.achieveitserver.request.laborhour;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ShowSubordinateLaborHourListRequest {

	private Integer id;
	
	private Long date;
	
	private Integer functionId;
	
	private String functionName;
	
	private Integer subfunctionId;
	
	private String subfunctionName;
	
	private Integer activityId;
	
	private String activityName;
	
	private Integer subactivityId;
	
	private String subactivityName;

    private Long startTime;

    private Long endTime;
    
    private Long submissionDate;
    
    private String state;
    
    private String submitterName;
    
    private String projectName;
}
