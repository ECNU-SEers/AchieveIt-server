package pretty.april.achieveitserver.request;

import lombok.Data;

import java.util.List;

@Data
public class EditMemberRequest {

    private Integer leaderId;
    private String leaderName;
    private List<Integer> roles;
}
