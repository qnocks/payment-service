package com.itransition.payment.auth.security.jwt;

import com.itransition.payment.auth.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt.token.secret}")
    private String secret;

    @Value("${app.jwt.token.expired}")
    private long expiredMs;

    public String createToken(String username, List<Role> roles) {
        val claims = Jwts.claims().setSubject(username);
        claims.put("roles", getRoleNames(roles));

        val creationDate = new Date();
        val expirationDate = new Date(creationDate.getTime() + expiredMs);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(creationDate)
                .setExpiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public boolean validateToken(String token) {
        return getClaimsFromToken(token)
                .getExpiration()
                .after(new Date());
    }

    public String getSubject(String token) {
        return getClaimsFromToken(token)
                .getSubject();
    }

    public Claims getClaimsFromToken(String token) {
        val key = Base64.getEncoder().encodeToString(secret.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
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
