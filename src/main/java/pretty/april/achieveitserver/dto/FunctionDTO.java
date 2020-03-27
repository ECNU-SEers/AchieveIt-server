package pretty.april.achieveitserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FunctionDTO {
    private Integer id;
    private String name;
    private String description;
    private Integer numSubFunctions;
}
