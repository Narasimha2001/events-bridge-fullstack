package com.eb.eventsbridge.modules.core.dto.common;


public record UserSummaryDto(
        Long id,
        String fullName,
        String email,
        String role
) {}
