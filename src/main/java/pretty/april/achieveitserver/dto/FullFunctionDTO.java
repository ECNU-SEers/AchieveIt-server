package pretty.april.achieveitserver.dto;

import lombok.Data;

import java.util.List;

@Data
public class FullFunctionDTO {

    private Integer id;
    private String name;
    private String description;
    private List<FunctionDTO> subFunctions;
}
