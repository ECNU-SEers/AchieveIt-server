package pretty.april.achieveitserver.dto;

import lombok.Data;

import java.util.List;

@Data
public class DetailedViewRoleDTO {

    private Integer id;
    private String name;
    private String remark;
    private String creator;
    private List<ViewPermissionDTO> permissions;
}
