package pretty.april.achieveitserver.request.project;

import java.time.LocalDate;
import java.util.List;

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
public class CreateProjectRequest {
	
	private String outerId;
	private String name;
	private LocalDate startDate;
	private LocalDate endDate;
	private String clientOuterId;
	private String company;
	private Integer supervisorId;
	private String supervisorName;
	private Integer managerId;
	private String managerName;
	private List<String> skillNames;
	private String businessAreaName;
	private String remark;

}
