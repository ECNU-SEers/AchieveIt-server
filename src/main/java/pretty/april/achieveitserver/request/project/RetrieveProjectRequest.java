package pretty.april.achieveitserver.request.project;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pretty.april.achieveitserver.entity.Client;
import pretty.april.achieveitserver.entity.Milestone;
import pretty.april.achieveitserver.entity.Project;
import pretty.april.achieveitserver.entity.ProjectBusinessArea;
import pretty.april.achieveitserver.entity.ProjectFunction;
import pretty.april.achieveitserver.entity.ProjectSkill;

/**
 * 
 * @author chenjialei
 *
 */
@Setter
@Getter
@NoArgsConstructor
public class RetrieveProjectRequest {
	
	private Project project;
	private Client projectClient;
	private List<ProjectSkill> projectSkills;
	private ProjectBusinessArea projectBusinessArea;
	private List<Milestone> projectMilestones;
	private List<ProjectFunction> projectFunctions;
	
}
