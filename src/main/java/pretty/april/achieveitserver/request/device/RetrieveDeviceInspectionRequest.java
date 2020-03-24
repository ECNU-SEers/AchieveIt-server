package pretty.april.achieveitserver.request.device;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RetrieveDeviceInspectionRequest {

	private LocalDate inspectDate;
    
    private String intact;

    private String remark;
}
