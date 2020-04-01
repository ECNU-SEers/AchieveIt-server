package pretty.april.achieveitserver.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Data
@AllArgsConstructor
public class UserContext {
    private Integer userId;
    private String username;
//    private List<GrantedAuthority> authorities;
}
