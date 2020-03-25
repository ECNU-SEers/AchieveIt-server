package pretty.april.achieveitserver.request.statechange;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RetrieveStateChangeRequest {

	private Integer projectId;
    
    private LocalDateTime changeDate;
    
    private String formerState;
    
    private String latterState;
    
    private Integer operatorId;
    
    private String operation;
}
