package com.eb.eventsbridge.modules.core.dto.event;

import java.time.LocalDateTime;

import com.eb.eventsbridge.modules.core.dto.club.ClubSummaryDto;
import com.eb.eventsbridge.modules.core.entity.EventStatus;

public record EventDetailsDTO(
	    Long id,
	    String title,
	    String description,
	    String location,
	    Integer capacity,
	    Long registrationCount,
	    EventStatus status,
	    String bannerImageUrl,
	    LocalDateTime startTime,
	    LocalDateTime endTime,
	    ClubSummaryDto club
	) {}