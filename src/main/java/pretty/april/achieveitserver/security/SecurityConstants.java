package pretty.april.achieveitserver.security;

public interface SecurityConstants {

    String JWT_TOKEN_HEADER = "Authorization";

    String JWT_TOKEN_HEADER_PREFIX = "Bearer ";

    String TOKEN_LOGIN_URL = "/api/auth/login";

    String JWT_TOKEN_REFRESH_URL = "/api/auth/token";

    String JWT_TOKEN_REQUIRED_URL = "/api/**";

    String SCOPES_REFRESH_TOKEN = "ROLE_REFRESH_TOKEN";
}
