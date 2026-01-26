package com.eb.eventsbridge.modules.auth.mapper;

import org.springframework.stereotype.Component;

import com.eb.eventsbridge.modules.auth.dto.register.RegisterResponseDTO;
import com.eb.eventsbridge.modules.auth.entity.User;

@Component
public class RegistrationMapper {

    public RegisterResponseDTO toDTO(User savedUser) {
        RegisterResponseDTO registerResponseDTO = new RegisterResponseDTO();

        registerResponseDTO.setId(savedUser.getId().toString());
        registerResponseDTO.setEmail(savedUser.getEmail());
        registerResponseDTO.setFullName(savedUser.getFullName());
        registerResponseDTO.setRoleName(savedUser.getRole().getName());
        return registerResponseDTO;

    }
}
