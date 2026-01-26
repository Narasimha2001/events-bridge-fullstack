package com.eb.eventsbridge.modules.core.dto.analytics;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventPerformanceDTO {
	private Long eventId;
	private String title;
	private long registrationCount;
	private Double averageRating;
}