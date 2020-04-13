package pretty.april.achieveitserver.request.dict;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UpdateSkillDictRequest {

	private Integer skillId;
	private String newSkillName;
	
}
