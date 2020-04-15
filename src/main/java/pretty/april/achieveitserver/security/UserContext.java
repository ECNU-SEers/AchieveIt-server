package pretty.april.achieveitserver.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserContext {
    private Integer userId;
    private String username;
//    private List<GrantedAuthority> authorities;
}
