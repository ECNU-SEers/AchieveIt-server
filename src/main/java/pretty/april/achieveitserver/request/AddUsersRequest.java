package pretty.april.achieveitserver.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class AddUsersRequest {

    @NotEmpty
    private List<String> usernames;
}
