package com.eb.eventsbridge.modules.core.dto.registration;


import java.time.LocalDateTime;

import com.eb.eventsbridge.modules.core.entity.RegistrationStatus;

public record RegistrationListDTO(
    Long id,
    RegistrationStatus status,
    LocalDateTime registeredAt,
    
    Long eventId,
    String eventTitle,
    String eventLocation,
    LocalDateTime eventStartTime,
    String eventBannerUrl
) {}