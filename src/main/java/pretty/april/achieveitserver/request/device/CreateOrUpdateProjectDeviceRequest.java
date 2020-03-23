package pretty.april.achieveitserver.request.device;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CreateOrUpdateProjectDeviceRequest {

	private String outerId;
	
	private String type;

    private Integer projectId;

    private Integer managerId;
    
    private String managerName;

    private LocalDate startDate;

    private LocalDate dueDate;
	
}
