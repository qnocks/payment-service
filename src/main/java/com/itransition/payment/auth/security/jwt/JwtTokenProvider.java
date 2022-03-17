package com.itransition.payment.auth.security.jwt;

import com.itransition.payment.auth.dto.TokenPayload;
import com.itransition.payment.auth.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private static final String ROLES_CLAIMS_KEY = "roles";

    @Value("${app.auth.jwt.token.secret}")
    private String secret;

    @Value("${app.auth.jwt.token.expired}")
    private long expired;

    public TokenPayload createToken(String username, List<Role> roles) {
        val claims = Jwts.claims().setSubject(username);
        claims.put(ROLES_CLAIMS_KEY, getRoleNames(roles));

        val creationDate = LocalDateTime.now();
        val expirationDate = creationDate.plusSeconds(expired);

        return TokenPayload.builder()
                .token(Jwts.builder()
                        .setClaims(claims)
                        .setSubject(username)
                        .setIssuedAt(Date.from(creationDate.toInstant(ZoneOffset.UTC)))
                        .setExpiration(Date.from(expirationDate.toInstant(ZoneOffset.UTC)))
                        .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                        .compact())
                .expiration(expirationDate)
                .build();
    }

    public boolean validateToken(String token) {
        return getClaimsFromToken(token)
                .getExpiration()
                .after(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
    }

    public String getSubject(String token) {
        return getClaimsFromToken(token)
                .getSubject();
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private List<String> getRoleNames(List<Role> roles) {
        val result = new ArrayList<String>();
        roles.forEach(role -> result.add(role.getName()));
        return result;
    }
}
