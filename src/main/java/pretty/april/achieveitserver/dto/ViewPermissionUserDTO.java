package pretty.april.achieveitserver.dto;

import lombok.Data;

import java.util.List;

@Data
public class ViewPermissionUserDTO {
    private Integer id;
    private String username;
    private String email;
    private String department;
    private String phoneNumber;
    private String realName;
    private List<ViewRoleDTO> roles;
    private List<String> projects;
}
