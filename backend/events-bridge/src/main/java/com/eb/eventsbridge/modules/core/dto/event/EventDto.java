package com.eb.eventsbridge.modules.core.dto.event;


import java.time.LocalDateTime;

public record EventDto(
    Long id,
    String title,
    String description,
    String location,
    LocalDateTime startTime,
    LocalDateTime endTime,
    Integer capacity,
    String bannerImageUrl
    
) {}