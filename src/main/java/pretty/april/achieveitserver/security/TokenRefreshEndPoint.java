package pretty.april.achieveitserver.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pretty.april.achieveitserver.dto.Response;
import pretty.april.achieveitserver.entity.User;
import pretty.april.achieveitserver.enums.ErrorCode;
import pretty.april.achieveitserver.mapper.UserMapper;
import pretty.april.achieveitserver.utils.ResponseUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TokenRefreshEndPoint {

    private JwtTokenHelper jwtTokenHelper;

    private UserMapper userMapper;

    public TokenRefreshEndPoint(JwtTokenHelper jwtTokenHelper, JwtTokenHelper jwtTokenFactory, UserMapper userMapper) {
        this.jwtTokenHelper = jwtTokenHelper;
        this.jwtTokenHelper = jwtTokenFactory;
        this.userMapper = userMapper;
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
        Integer userId = claims.getBody().get("userId", Integer.class);

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("id", userId).eq("username", subject));
        if (user == null) {
            return ResponseUtils.errorResponseWithMessage(ErrorCode.INVALID_TOKEN, "User not found");
        }

        UserContext userContext = new UserContext(userId, subject);
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getId() == 1) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return ResponseUtils.successResponse(new AccessToken(jwtTokenHelper.generateAccessJwtToken(userContext, authorities)));
    }

}
