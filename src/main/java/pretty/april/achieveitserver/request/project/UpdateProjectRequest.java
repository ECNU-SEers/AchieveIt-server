package pretty.april.achieveitserver.request.project;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author chenjialei
 *
 */
@Setter
@Getter
@NoArgsConstructor
public class UpdateProjectRequest {
	
	private String outerId;
	private String name;
	private String clientOuterId;
	private String company;
	private LocalDate startDate;
	private LocalDate endDate;
	private String milestone;
	private List<String> skillNames;
	private String businessAreaName;
	private String remark;

}
