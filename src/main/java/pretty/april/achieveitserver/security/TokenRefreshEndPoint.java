package pretty.april.achieveitserver.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pretty.april.achieveitserver.dto.Response;
import pretty.april.achieveitserver.enums.ErrorCode;
import pretty.april.achieveitserver.service.UserService;
import pretty.april.achieveitserver.utils.ResponseUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TokenRefreshEndPoint {

    private JwtTokenHelper jwtTokenHelper;

    private UserService userService;

    public TokenRefreshEndPoint(JwtTokenHelper jwtTokenHelper, UserService userService, JwtTokenHelper jwtTokenFactory) {
        this.jwtTokenHelper = jwtTokenHelper;
        this.userService = userService;
        this.jwtTokenHelper = jwtTokenFactory;
    }


    @GetMapping("/api/auth/token")
    public Response<?> refreshToken(HttpServletRequest request) {
        String tokenPayload = request.getHeader(SecurityConstants.JWT_TOKEN_HEADER);
        String token = tokenPayload.substring(SecurityConstants.JWT_TOKEN_HEADER_PREFIX.length());

        Jws<Claims> claims = jwtTokenHelper.validateRefreshToken(token);
        if (claims == null) {
            return ResponseUtils.errorResponse(ErrorCode.INVALID_TOKEN);
        }

        // Simply check the presence of id
        if (StringUtils.isEmpty(claims.getBody().getId())) {
            return ResponseUtils.errorResponseWithMessage(ErrorCode.INVALID_TOKEN, "Token id invalid");
        }

        String subject = claims.getBody().getSubject();
        SecurityUser su = userService.getSecurityUserByUsername(subject);
        if (su == null) {
            return ResponseUtils.errorResponseWithMessage(ErrorCode.INVALID_TOKEN, "User not found");
        }
        if (CollectionUtils.isEmpty(su.getAuthorities())) {
            return ResponseUtils.errorResponseWithMessage(ErrorCode.INVALID_TOKEN, "User has no roles");
        }

        List<GrantedAuthority> authorities = su.getAuthorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        UserContext userContext = new UserContext(subject, authorities);
        return ResponseUtils.successResponse(new AccessToken(jwtTokenHelper.generateAccessJwtToken(userContext)));
    }

}
