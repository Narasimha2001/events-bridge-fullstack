package com.eb.eventsbridge.modules.core.dto.event;

import java.time.LocalDateTime;

import com.eb.eventsbridge.modules.core.entity.EventStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EventSummaryDTO {

    private Long eventId;
    private String title;
    private String location;
    private EventStatus status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String clubName;
    private String description;
    private Integer capacity;
}
