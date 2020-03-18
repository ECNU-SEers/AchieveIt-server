package pretty.april.achieveitserver.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "security.jwt")
@Data
public class JwtSettings {
    private int tokenExpirationMinutes;
    private String tokenIssuer;
    private String tokenSigningKey;
    private int refreshTokenExpirationMinutes;
}
