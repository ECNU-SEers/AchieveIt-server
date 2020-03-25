package pretty.april.achieveitserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import pretty.april.achieveitserver.enums.ErrorCode;
import pretty.april.achieveitserver.utils.ResponseUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    public AuthFailureHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);

        if (e instanceof BadCredentialsException) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            objectMapper.writeValue(httpServletResponse.getWriter(), ResponseUtils.errorResponseWithMessage(ErrorCode.INVALID_USERNAME_PASSWORD, e.getMessage()));
        } else if (e instanceof InvalidTokenException) {
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            objectMapper.writeValue(httpServletResponse.getWriter(), ResponseUtils.errorResponseWithMessage(ErrorCode.AUTHENTICATION_FAILED, e.getMessage()));
        } else {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            objectMapper.writeValue(httpServletResponse.getWriter(), ResponseUtils.errorResponse(ErrorCode.AUTHENTICATION_FAILED));
        }
    }
}
