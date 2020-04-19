package pretty.april.achieveitserver.request.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RetrieveBasicMemberInfoRequest {

	private Integer userId;

    private String username;
	
	private String userRealName;
}
