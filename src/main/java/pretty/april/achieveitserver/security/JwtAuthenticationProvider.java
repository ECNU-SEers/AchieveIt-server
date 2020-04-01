package pretty.april.achieveitserver.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtTokenHelper jwtTokenHelper;

    public JwtAuthenticationProvider(JwtTokenHelper jwtTokenHelper) {
        this.jwtTokenHelper = jwtTokenHelper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getCredentials();

        Jws<Claims> claimsJws = jwtTokenHelper.parseClaims(token);
        String subject = claimsJws.getBody().getSubject();
        Integer userId = claimsJws.getBody().get("userId", Integer.class);
        UserContext userContext = new UserContext(userId, subject);
        List<String> roles = claimsJws.getBody().get("roles", List.class);
        List<GrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        return new JwtAuthenticationToken(authorities, userContext);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return (JwtAuthenticationToken.class.isAssignableFrom(aClass));
    }
}
