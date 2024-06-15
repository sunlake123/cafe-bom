package com.kwy.cafebom.common.config.security;

import com.kwy.cafebom.auth.service.impl.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private static final String KEY_ROLE = "role";
    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24; // 1일 (24시간)

    private final AuthService authService;

    @Value("${spring.jwt.secret}")
    private String secretKey;

    public String generateToken(Long userId, String email, Role role) {
        Claims claims = Jwts.claims()
            .setSubject(email)
            .setId(userId + "");
        claims.put(KEY_ROLE, role);

        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expiredDate)
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = authService.loadUserByUsername(getEmail(token));
        ArrayList<GrantedAuthority> authorities = new ArrayList<>(userDetails.getAuthorities());
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    public String getEmail(String token) {
        return parseClaims(token).getSubject();
    }

    public Long getId(String token) {
        return Long.parseLong(parseClaims(removeBearerFromToken(token)).getId());
    }

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }

        Claims claims = parseClaims(token);
        return claims.getExpiration().before(new Date());
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 토큰입니다.");
            return e.getClaims();
        }
    }

    private String removeBearerFromToken(String token) {
        return token.substring(JwtAuthenticationFilter.TOKEN_PREFIX.length());
    }

}
