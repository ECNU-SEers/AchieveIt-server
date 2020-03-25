package pretty.april.achieveitserver.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class AddViewRoleRequest {

    @NotBlank
    private String name;

    @NotEmpty
    private List<Integer> permissions;

    private String remark;
}
