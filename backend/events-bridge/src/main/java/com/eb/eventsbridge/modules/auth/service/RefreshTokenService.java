package com.eb.eventsbridge.modules.auth.service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.eb.eventsbridge.modules.auth.entity.RefreshToken;
import com.eb.eventsbridge.modules.auth.entity.User;
import com.eb.eventsbridge.modules.auth.repository.RefreshTokenRepository;
import com.eb.eventsbridge.modules.auth.utils.JwtUtils;

import io.jsonwebtoken.JwtException;
import jakarta.transaction.Transactional;

@Service
public class RefreshTokenService {

    private final JwtUtils jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh_token.expiration}")
    private int refreshTokenExpiryInDays;


    public RefreshTokenService(JwtUtils jwtUtils, RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtils;
        this.refreshTokenRepository = refreshTokenRepository;
    }


    public RefreshToken generateRefreshToken(User user) {
        String token = this.jwtUtil.generateRefreshToken(user);

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setToken(token);
        refreshToken.setUser(user);
        refreshToken.setRevoked(false);
        
        this.refreshTokenRepository.save(refreshToken);

        return refreshToken;

    }

    public RefreshToken validateAndRotate(String token) {

        try {
            jwtUtil.validateToken(token);
        } catch (JwtException ex) {
            throw new RuntimeException("Refresh token invalid or expired");
        }

        RefreshToken oldRefreshToken = this.refreshTokenRepository.findByToken(token).
        		orElseThrow(() -> new RuntimeException("Old refresh token does not exists"));

               
        if (oldRefreshToken.isRevoked()) {
            // reuse detected — possible theft
            handleTokenReuse(oldRefreshToken);
            throw new RuntimeException("Refresh token reuse detected");
        }
        
        refreshTokenRepository.delete(oldRefreshToken);   

        //rotate - generate new refresh token
        return this.generateRefreshToken(oldRefreshToken.getUser());
    }

    
    private void handleTokenReuse(RefreshToken token) {
        User user = token.getUser();

        // revoke all refresh tokens for user
        List<RefreshToken> tokenList =
                refreshTokenRepository.findByUserAndIsRevokedFalse(user)
                        .stream()
                        .peek(t -> {
                            t.setRevoked(true);
                            t.setRevokedAt(Instant.now());
                        }).toList();

        refreshTokenRepository.saveAll(tokenList);
    }
    

    public Map<String, String> refresh(String oldRefreshToken) {

        RefreshToken newRefreshToken = this.validateAndRotate(oldRefreshToken);

        User user = newRefreshToken.getUser();

        String accessToken = this.jwtUtil.generateToken(user);

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", accessToken);
        response.put("refreshToken", newRefreshToken.getToken());

        return response;
    }
    
    @Transactional
    public void logout(String refreshToken) {
        this.refreshTokenRepository.deleteByToken(refreshToken);
    }
}
