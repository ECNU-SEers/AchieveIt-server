package pretty.april.achieveitserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleMemberDTO {
    private Integer id;
    private String username;
    private String realName;
}
