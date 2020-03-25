package pretty.april.achieveitserver.request;

import lombok.Data;

import java.util.List;

@Data
public class EditViewRoleRequest {

    private String name;
    private List<Integer> permissions;
    private String remark;
}
