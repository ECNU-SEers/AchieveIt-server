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

	/**
	 * 项目基本信息
	 */
	private RetrieveProjectRequest projectInfo;
	
	/**
	 * 项目立项中项目上级的审核结果
	 */
	private boolean reviewResult;
}
