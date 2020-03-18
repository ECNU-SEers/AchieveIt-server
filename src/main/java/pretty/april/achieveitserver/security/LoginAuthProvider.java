package pretty.april.achieveitserver.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import pretty.april.achieveitserver.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LoginAuthProvider implements AuthenticationProvider {

    private final UserService userService;
    private final BCryptPasswordEncoder encoder;

    public LoginAuthProvider(UserService userService, BCryptPasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.notNull(authentication, "No auth data provided");

        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        SecurityUser su = userService.getSecurityUserByUsername(username);
        if (su == null) {
            throw new UsernameNotFoundException("Cannot find user: " + username);
        }

        if (!encoder.matches(password, su.getPassword())) {
            throw new BadCredentialsException("Username or password is invalid");
        }

        if (su.getRoles() == null) {
            throw new InsufficientAuthenticationException("User has no roles");
        }

        List<GrantedAuthority> authorities = su.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        UserContext uc = new UserContext(su.getUsername(), authorities);

        return new UsernamePasswordAuthenticationToken(uc, null, authorities);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
