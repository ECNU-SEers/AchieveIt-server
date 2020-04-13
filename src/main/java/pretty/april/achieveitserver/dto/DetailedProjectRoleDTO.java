package pretty.april.achieveitserver.dto;

import lombok.Data;

import java.util.List;

@Data
public class DetailedProjectRoleDTO {

    private Integer id;
    private String name;
    private String remark;
    private String creator;
    private List<PermissionDTO> permissions;
}
