package com.epam.esm.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Class {@code JWTProvider} contains operations with JSON web tokens.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
@Component
public class JWTProvider {
    /**
     * long expirationInMinutes.
     */
    @Value("${jwt.expiration}")
    private long expirationInMinutes;
    /**
     * String jwtSecret.
     */
    @Value("${jwt.secret}")
    private String jwtSecret;

    public JWTProvider() {
    }

    /**
     * This method generates a unique user token after authorization.
     *
     * @param email unique user identifier
     * @return generated token
     */
    public String generateToken(String email) {
        Date date = Date.from(LocalDateTime.now().plusMinutes(expirationInMinutes).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     * This method validates the token.
     *
     * @param token the unique user token
     * @return {@code true} if the token is valid, {@code false} otherwise
     */
    @SneakyThrows
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * This method decrypts the login from the token.
     *
     * @param token the unique user token
     * @return user login
     */
    public String getLoginFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
