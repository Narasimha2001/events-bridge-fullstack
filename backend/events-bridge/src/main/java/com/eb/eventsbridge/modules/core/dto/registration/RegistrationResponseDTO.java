package com.eb.eventsbridge.modules.core.dto.registration;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistrationResponseDTO {
	private Long registrationId;
	private String eventTitle;
	private Long eventId;
	private LocalDateTime eventStartTime;
	private String venue;
	private String status; // CONFIRMED, CANCELLED, etc.
	private LocalDateTime registeredAt;
}