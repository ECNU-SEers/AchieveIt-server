package pretty.april.achieveitserver.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class EditRoleRequest {

    @NotBlank
    private String name;

    private String remark;

    private List<Integer> permissions;
}
