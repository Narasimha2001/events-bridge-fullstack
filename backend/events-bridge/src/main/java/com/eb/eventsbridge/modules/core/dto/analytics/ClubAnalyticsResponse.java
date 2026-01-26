package com.eb.eventsbridge.modules.core.dto.analytics;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClubAnalyticsResponse {
	private Long clubId;
	private String clubName;
	private long totalEvents;
	private long totalRegistrations;
	private Double averageRating;
	private List<EventPerformanceDTO> topEvents;
}

