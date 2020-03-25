package pretty.april.achieveitserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ViewRoleDTO {

    private Integer id;
    private String name;
    private String remark;
}
