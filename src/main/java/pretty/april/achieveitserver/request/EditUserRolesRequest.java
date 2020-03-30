package pretty.april.achieveitserver.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class EditUserRolesRequest {

    @NotNull
    private List<Integer> roles;
}
