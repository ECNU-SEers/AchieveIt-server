package pretty.april.achieveitserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private RestAuthenticationEntryPoint authenticationEntryPoint;
    private AuthenticationSuccessHandler successHandler;
    private AuthenticationFailureHandler failureHandler;
    private LoginAuthProvider loginAuthProvider;
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    private ObjectMapper objectMapper;

    public WebSecurityConfig(RestAuthenticationEntryPoint authenticationEntryPoint, AuthenticationSuccessHandler successHandler, AuthenticationFailureHandler failureHandler, LoginAuthProvider loginAuthProvider, JwtAuthenticationProvider jwtAuthenticationProvider, ObjectMapper objectMapper) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.loginAuthProvider = loginAuthProvider;
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
        this.objectMapper = objectMapper;
    }

    protected LoginProcessingFilter loginProcessingFilter() {
        LoginProcessingFilter filter = new LoginProcessingFilter(SecurityConstants.TOKEN_LOGIN_URL, successHandler, failureHandler, objectMapper);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    protected JwtTokenAuthProcessingFilter jwtTokenAuthProcessingFilter() {
        List<String> pathsToSkip = Arrays.asList(SecurityConstants.JWT_TOKEN_REFRESH_URL, SecurityConstants.TOKEN_LOGIN_URL);
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, SecurityConstants.JWT_TOKEN_REQUIRED_URL);
        JwtTokenAuthProcessingFilter filter = new JwtTokenAuthProcessingFilter(matcher, failureHandler);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(loginAuthProvider);
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(this.authenticationEntryPoint)
                .accessDeniedHandler(new NoAuthorityHandler(objectMapper))

                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, SecurityConstants.TOKEN_LOGIN_URL).permitAll()
                .antMatchers(HttpMethod.GET, SecurityConstants.JWT_TOKEN_REFRESH_URL).permitAll()

                .and()
                .authorizeRequests()
                .antMatchers(SecurityConstants.JWT_TOKEN_REQUIRED_URL).authenticated()

                .and()
                .addFilterBefore(loginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtTokenAuthProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}