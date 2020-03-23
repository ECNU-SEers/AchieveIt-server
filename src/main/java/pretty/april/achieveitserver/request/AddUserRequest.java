package pretty.april.achieveitserver.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class AddUserRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @Email
    private String email;

    @NotBlank
    private String department;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String realName;
}
