package pretty.april.achieveitserver.request.dict;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UpdateBusinessAreaDictRequest {
	
	private Integer businessAreaId;
	private String newBusinessAreaName;

}
