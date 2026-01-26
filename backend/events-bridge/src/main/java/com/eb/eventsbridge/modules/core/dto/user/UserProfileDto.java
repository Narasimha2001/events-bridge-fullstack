package com.eb.eventsbridge.modules.core.dto.user;


public record UserProfileDto(
    Long id,
    String fullName,
    String email,
    String role,
    Long totalActivityCount 
) {}