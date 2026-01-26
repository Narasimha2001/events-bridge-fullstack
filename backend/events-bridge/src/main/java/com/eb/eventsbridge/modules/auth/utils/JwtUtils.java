package com.eb.eventsbridge.modules.auth.utils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.eb.eventsbridge.modules.auth.entity.RefreshToken;
import com.eb.eventsbridge.modules.auth.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtUtils {

    private final Key secretKey;
    @Value("${jwt.access_token.expiration}")
    private int accessTokenExpiryInMinutes;

    @Value("${jwt.refresh_token.expiration}")
    private int refreshTokenExpiryInDays;

    public JwtUtils(@Value("${jwt.secret}") String secret) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }


    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("role",user.getRole().getName())
                .issuedAt(new Date())
                .expiration(
                        Date.from(Instant.now().plus(accessTokenExpiryInMinutes,
                                ChronoUnit.MINUTES))
                )
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("role", user.getRole().getName())
                .issuedAt(Date.from(Instant.now()))
                .expiration(
                        Date.from(Instant.now().plus(refreshTokenExpiryInDays,
                                ChronoUnit.DAYS))
                )
                .signWith(secretKey)
                .compact();
    }

    public void validateToken(String token) {

        try {
            //  does full signature + expiry + structure validation
            Jwts.parser().verifyWith((SecretKey) this.secretKey).build()
                    .parseSignedClaims(token);
        } catch (SignatureException e) {
            throw new JwtException("Invalid JWT signature");
        } catch (JwtException e) {
            throw new JwtException("Invalid JWT");
        }

    }
    
    public String extractEmail(String token) {
    	String email = null;
    	try {
    		email = Jwts.parser().verifyWith((SecretKey) this.secretKey).build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
    	}
    	catch(Exception ex) {
    		throw new JwtException(ex.getMessage());
    	}
    	return email;
        
    }


}