package pretty.april.achieveitserver.request.client;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RetrieveClientInfoRequest {

	private String outerId;
	private String company;
	
}
