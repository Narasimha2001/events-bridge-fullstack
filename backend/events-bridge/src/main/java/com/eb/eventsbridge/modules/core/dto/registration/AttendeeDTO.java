package com.eb.eventsbridge.modules.core.dto.registration;


import java.time.LocalDateTime;

import com.eb.eventsbridge.modules.core.entity.RegistrationStatus;

public record AttendeeDTO(
    Long registrationId,
    String studentName,
    String studentEmail,
    LocalDateTime registeredAt,
    RegistrationStatus status,
    Long studentId
) {}