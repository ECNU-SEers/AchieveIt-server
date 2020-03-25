package pretty.april.achieveitserver.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddFunctionRequest {

    @NotBlank
    private String name;

    private String description;

    private Integer parentId;
}
