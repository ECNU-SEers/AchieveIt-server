package pretty.april.achieveitserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ViewPermissionDTO {
    private Integer id;
    private String permission;
    private String module;
}
