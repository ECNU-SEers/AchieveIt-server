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
public class ObtainAllProjectRequest {

	private Integer id;
	private String outerId;
	private String name;
}
