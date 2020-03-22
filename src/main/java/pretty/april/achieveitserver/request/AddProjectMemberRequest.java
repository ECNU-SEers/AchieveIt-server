package pretty.april.achieveitserver.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AddProjectMemberRequest {
    @NotNull
    private Integer userId;

    @NotBlank
    private String username;

    private Integer leaderId;

    private String leaderName;

    private List<Integer> roleId;
}
