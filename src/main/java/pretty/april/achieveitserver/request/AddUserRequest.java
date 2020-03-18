package pretty.april.achieveitserver.request;

import lombok.Data;

@Data
public class AddUserRequest {
    private String username;
    private String password;
    private String email;
    private String department;
}
