package com.sfmckenrick.assessment.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Utility Class for creating and parsing JWT Tokens.
 * @author Scott McKenrick <sbm5967@arl.psu.edu>
 */
@Component
public class JwtUtil {

    /**
     * The key to use for adding the Authorities to the JWT Claims.
     */
    private final String AUTHORITIES_KEY = "authorities";

    /**
     * The secret to use when encrypting the token.
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * The time that the token is valid. In millis.
     */
    @Value("${jwt.valid.millis}")
    private Long tokenExpiryMillis;

    /**
     * Attempts to parse and validate a JWT. Returns an Optional-wrapped User representing the token's owner.
     *
     * @param token the JWT token to parse
     * @return The username of the requesting user.
     */
    public Optional<String> extractUsername(String token) {
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            String username = body.getSubject();

            return Optional.of(username);

        } catch (JwtException e) {
            return Optional.empty();
        }
    }


    /**
     * Generates a simple JWT with a Username subject and an Expiry time.
     *
     * @param user - The User for which the token will be generated
     * @return the JWT token
     */
    public String generateToken(UserDetails user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put(AUTHORITIES_KEY, user.getAuthorities());

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime expirationDateTime = now.plus(this.tokenExpiryMillis, ChronoUnit.MILLIS);

        Date issueDate = Date.from(now.toInstant());
        Date expirationDate = Date.from(expirationDateTime.toInstant());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .setIssuedAt(issueDate).setExpiration(expirationDate)
                .compact();
    }
}
