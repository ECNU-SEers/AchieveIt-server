package pretty.april.achieveitserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleUserDTO {
    private Integer userId;
    private String username;
    private String realName;
}
