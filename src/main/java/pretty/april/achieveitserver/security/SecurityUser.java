package pretty.april.achieveitserver.security;

import lombok.Data;

import java.util.Collection;

@Data
public class SecurityUser {
    private String username;
    private String password;
    private Collection<String> authorities;
}