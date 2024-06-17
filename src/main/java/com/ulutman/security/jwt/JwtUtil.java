package com.ulutman.security.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    @Value("${jwt.secret:defaultSecretKeyThatIsLongEnoughForHS256}")
    private String secretKey;
    private final Long TOKEN_EXPIRATION = 24 * 7 * 60 * 60 * 1000L; // 7 days

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaimsFromToken(String token, Function<Claims, T> function) {
        final Claims claims = getAllClaimsFromToken(token);
        return function.apply(claims);
    }

    public Date getExpirationDateToken(String token) {
        return getClaimsFromToken(token, Claims::getExpiration);
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateToken(token);
        return expiration.before(new Date());
    }

    public String getUserNameFromToken(String token) {
        return getClaimsFromToken(token, Claims::getSubject);
    }

    public Boolean tokenIsValidation(String token, UserDetails userDetails) {
        final String userName = getUserNameFromToken(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
