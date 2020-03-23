package pretty.april.achieveitserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleMemberDTO {
    private Integer userId;
    private String username;
}
