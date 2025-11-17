package com.futurework.hub.shared.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtTokenService {

    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final long expirationMinutes;

    public JwtTokenService(@Value("${security.jwt.secret:change-me}") String secret,
                           @Value("${security.jwt.expiration-minutes:120}") long expirationMinutes) {
        this.algorithm = Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));
        this.verifier = JWT.require(this.algorithm).withIssuer("future-work-hub").build();
        this.expirationMinutes = expirationMinutes;
    }

    public String generateToken(UserDetails userDetails) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(expirationMinutes, ChronoUnit.MINUTES);

        return JWT.create()
                .withIssuer("future-work-hub")
                .withSubject(userDetails.getUsername())
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(expiresAt))
                .sign(algorithm);
    }

    public String extractUsername(String token) {
        return decode(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isExpired(token);
    }

    private boolean isExpired(String token) {
        return decode(token).getExpiresAt().before(new Date());
    }

    private DecodedJWT decode(String token) {
        try {
            return verifier.verify(token);
        } catch (JWTVerificationException ex) {
            throw new JWTVerificationException("Invalid or expired token");
        }
    }
}

