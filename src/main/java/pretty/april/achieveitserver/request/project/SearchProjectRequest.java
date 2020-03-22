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
public class SearchProjectRequest {

	private String outerId;
	private String name;
}
