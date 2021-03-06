package pretty.april.achieveitserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {

    private Integer id;
    private String name;
    private String remark;
}
