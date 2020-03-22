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
public class AssignRoleRequest {

	private String outerId;
	private List<Integer> userId;
}
