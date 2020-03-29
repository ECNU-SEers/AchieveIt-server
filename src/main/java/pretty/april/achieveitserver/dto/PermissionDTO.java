package pretty.april.achieveitserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDTO {

    private Integer id;
    private String name;
    private String module;
    private String remark;
}
