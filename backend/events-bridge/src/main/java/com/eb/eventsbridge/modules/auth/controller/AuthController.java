package com.eb.eventsbridge.modules.auth.controller;

import com.eb.eventsbridge.modules.auth.dto.login.LoginRequestDTO;

import com.eb.eventsbridge.modules.auth.dto.login.LoginResponseDTO;
import com.eb.eventsbridge.modules.auth.dto.register.RegisterRequestDTO;
import com.eb.eventsbridge.modules.auth.dto.register.RegisterResponseDTO;
import com.eb.eventsbridge.modules.auth.service.AuthService;
import com.eb.eventsbridge.modules.auth.service.RefreshTokenService;
import com.eb.eventsbridge.shared.utils.AppConstants;

import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> registerUser(
            @Validated({Default.class}) @RequestBody RegisterRequestDTO registerRequestDTO) {

        return ResponseEntity.ok().body(this.authService.registerUser(registerRequestDTO));

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO response = this.authService.login(loginRequestDTO);

        ResponseCookie cookie = ResponseCookie.from(AppConstants.REFRESH_TOKEN_COOKIE, response.getRefreshToken())
                .httpOnly(true)
                .secure(false) // false for localhost
                .path("/")     // Making cookie available everywhere
                .maxAge(7 * 24 * 60 * 60) // 7 Days
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }
    

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(
            @CookieValue(value = AppConstants.REFRESH_TOKEN_COOKIE, required = false) String token
    ) {
        if (token == null) {
            throw new RuntimeException("Provide the refresh token");
        }

        Map<String, String> tokens = this.refreshTokenService.refresh(token);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", tokens.get(
        		AppConstants.REFRESH_TOKEN_COOKIE))
                .httpOnly(true)
                .secure(false)
                .sameSite("strict")
                .build();
        
        Map<String,String> resp = new HashMap<String, String>();
        
        resp.put("accessToken", tokens.get("accessToken"));

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(resp);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = AppConstants.REFRESH_TOKEN_COOKIE, required = false) String refreshToken
    ) {
        
        if (refreshToken != null) {
            this.refreshTokenService.logout(refreshToken);
        }
        
        // Clear the cookie in the browser
        ResponseCookie cookie = ResponseCookie.from(AppConstants.REFRESH_TOKEN_COOKIE, "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0) // Expire it
                .sameSite("Lax")
                .build();

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

}
