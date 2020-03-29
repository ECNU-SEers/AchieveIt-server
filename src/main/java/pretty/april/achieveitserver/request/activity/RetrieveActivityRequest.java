package pretty.april.achieveitserver.request.activity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RetrieveActivityRequest {
	
	private Integer id;
	private String name;
}
