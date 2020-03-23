package pretty.april.achieveitserver.request.device;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ShowProjectDeviceListRequest {

	private String outerId;
	
	private String type;

    private Integer managerId;
    
    private String managerName;

    private LocalDate startDate;

    private LocalDate dueDate;
    
    private String state;
    
    private String returnDate;
}
