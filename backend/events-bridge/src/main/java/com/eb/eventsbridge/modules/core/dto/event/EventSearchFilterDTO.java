package com.eb.eventsbridge.modules.core.dto.event;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.eb.eventsbridge.modules.core.entity.EventStatus;

import lombok.Data;

@Data
public class EventSearchFilterDTO {
	private String title;
	private Long clubId;
	private String venue;
	
	// Tell Spring to parse ISO Date Time format
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) 
    private LocalDateTime startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) 
    private LocalDateTime endDate;
	private EventStatus status;
}