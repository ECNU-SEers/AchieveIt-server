package pretty.april.achieveitserver.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtTokenHelper {
    private final JwtSettings settings;
    private final SecretKey secretKey;
    private final JwtParser jwtParser;

    public JwtTokenHelper(JwtSettings settings) {
        this.settings = settings;
        secretKey = Keys.hmacShaKeyFor(settings.getTokenSigningKey().getBytes(StandardCharsets.UTF_8));
        jwtParser = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build();
    }

    public String generateAccessJwtToken(UserContext userContext) {
        if (StringUtils.isEmpty(userContext.getUsername())) {
            throw new IllegalArgumentException("Cannot generate jwt token without username");
        }

        if (CollectionUtils.isEmpty(userContext.getAuthorities())) {
            throw new IllegalArgumentException("Cannot generate jwt token for user without any roles");
        }

        Claims claims = Jwts.claims().setSubject(userContext.getUsername());
        claims.put("scopes", userContext.getAuthorities().stream().map(Object::toString).collect(Collectors.toList()));

        LocalDateTime time = LocalDateTime.now();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(settings.getTokenIssuer())
                .setIssuedAt(Date.from(time.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(time.plusMinutes(settings.getTokenExpirationMinutes()).atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String username) {
        if (StringUtils.isEmpty(username)) {
            throw new IllegalArgumentException("Cannot generate jwt token without username");
        }

        LocalDateTime time = LocalDateTime.now();

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("scopes", Collections.singletonList(SecurityConstants.SCOPES_REFRESH_TOKEN));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(settings.getTokenIssuer())
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(time.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(java.util.Date.from(time.plusMinutes(settings.getRefreshTokenExpirationMinutes()).atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parseClaims(String token) {
        try {
            return jwtParser.parseClaimsJws(token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException | ExpiredJwtException e) {
            throw new BadCredentialsException("Invalid jwt token: ", e);
        }
    }

    @SuppressWarnings("unchecked")
    public Jws<Claims> validateRefreshToken(String token) {
        Jws<Claims> claims = null;
        try {
            claims = jwtParser.parseClaimsJws(token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException | ExpiredJwtException e) {
            return null;
        }
        List<String> scopes = claims.getBody().get("scopes", List.class);
        if (CollectionUtils.isEmpty(scopes) || scopes.stream().noneMatch(SecurityConstants.SCOPES_REFRESH_TOKEN::equals)) {
            return null;
        } else {
            return claims;
        }
    }
}
