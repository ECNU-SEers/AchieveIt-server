package pretty.april.achieveitserver.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import pretty.april.achieveitserver.entity.User;
import pretty.april.achieveitserver.service.UserService;

import java.util.ArrayList;
import java.util.List;

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

        User user = userService.getByUsername(username);
        if (user == null) {
            throw new BadCredentialsException("Cannot find user: " + username);
        }

        if (!encoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Username or password is invalid");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getId() == 1) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        UserContext uc = new UserContext(user.getId(), user.getUsername());

        return new UsernamePasswordAuthenticationToken(uc, null, authorities);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
