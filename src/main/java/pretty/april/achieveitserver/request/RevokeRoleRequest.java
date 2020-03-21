package pretty.april.achieveitserver.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RevokeRoleRequest {

    @NotNull
    private Integer userId;
}
