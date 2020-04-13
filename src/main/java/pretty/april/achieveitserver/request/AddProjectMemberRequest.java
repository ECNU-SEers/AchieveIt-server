package pretty.april.achieveitserver.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AddProjectMemberRequest {

    @NotBlank
    private String username;

    private String leaderName;

    private List<Integer> roleId;
}
