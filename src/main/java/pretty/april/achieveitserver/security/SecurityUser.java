package pretty.april.achieveitserver.security;

import lombok.Data;

import java.util.Collection;
import java.util.List;

@Data
public class SecurityUser {
    private String username;
    private String password;
    private Collection<String> authorities;
}