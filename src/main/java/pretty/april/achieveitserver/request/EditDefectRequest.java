package pretty.april.achieveitserver.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class EditDefectRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Integer type;

    @NotNull
    private Integer level;
}
