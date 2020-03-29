package pretty.april.achieveitserver.request.project;

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
public class ApproveProjectRequest {
	
	private String projectOuterId;
	
	/**
	 * 项目立项中项目上级的审核结果
	 */
	private boolean reviewResult;
}
