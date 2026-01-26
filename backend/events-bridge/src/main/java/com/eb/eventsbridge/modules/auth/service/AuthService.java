package com.eb.eventsbridge.modules.auth.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.eb.eventsbridge.modules.auth.dto.login.LoginRequestDTO;
import com.eb.eventsbridge.modules.auth.dto.login.LoginResponseDTO;
import com.eb.eventsbridge.modules.auth.dto.register.RegisterRequestDTO;
import com.eb.eventsbridge.modules.auth.dto.register.RegisterResponseDTO;
import com.eb.eventsbridge.modules.auth.entity.RefreshToken;
import com.eb.eventsbridge.modules.auth.entity.Role;
import com.eb.eventsbridge.modules.auth.entity.User;
import com.eb.eventsbridge.modules.auth.mapper.RegistrationMapper;
import com.eb.eventsbridge.modules.auth.repository.RoleRepository;
import com.eb.eventsbridge.modules.auth.repository.UserRepository;
import com.eb.eventsbridge.modules.auth.utils.JwtUtils;
import com.eb.eventsbridge.modules.core.dto.common.UserSummaryDto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RegistrationMapper regMapper;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    
    @Transactional
    public RegisterResponseDTO registerUser(RegisterRequestDTO reqDTO) {
        if (userRepo.existsByEmail(reqDTO.getEmail())) {
            throw new RuntimeException("User already registered!");
        }
        User user = new User();
        user.setEmail(reqDTO.getEmail());
        user.setFullName(reqDTO.getFullName());

        Role role = roleRepo.findByName(reqDTO.getRoleName())
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRole(role);
        user.setPasswordHash(passwordEncoder.encode(reqDTO.getPassword()));
        userRepo.save(user);
        
        return regMapper.toDTO(user);
    }
    
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        User user = this.userRepo.findByEmail(loginRequestDTO.getEmail())
        		.orElseThrow(() -> 
        			new RuntimeException("User not found")
        		);
        
        if (!this.passwordEncoder.matches(loginRequestDTO.getPassword(),
                user.getPasswordHash()) ) {
            throw new RuntimeException("Email or Password is invalid");
        }

        String accessToken = this.jwtUtils.generateToken(user);

        RefreshToken refreshToken = this.refreshTokenService.generateRefreshToken(user);

        
        UserSummaryDto userDto = new UserSummaryDto(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().getName()
        );
        
        return new LoginResponseDTO(accessToken, refreshToken.getToken(), userDto);
        
    }
    
    public boolean verifyToken(String token) {
        try {
            this.jwtUtils.validateToken(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    
    
}
