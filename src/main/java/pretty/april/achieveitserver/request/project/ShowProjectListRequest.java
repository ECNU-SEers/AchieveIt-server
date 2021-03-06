package pretty.april.achieveitserver.request.project;

import java.time.LocalDate;

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
public class ShowProjectListRequest {
	
	private Integer id;
	private String outerId;
	private String name;
	private String clientOuterId;
	private String company;
	private LocalDate startDate;
	private LocalDate endDate;
	private int supervisorId;
	private String supervisorName;
	private String supervisorRealName;
	private int managerId;
	private String managerName;
	private String managerRealName;
	private int participantCounter;
	private String qaAssigned;
	private String epgAssigned;
	private String state;
	
	/**
	 * 项目立项中项目上级的审核结果
	 */
	private boolean reviewResult;
}
