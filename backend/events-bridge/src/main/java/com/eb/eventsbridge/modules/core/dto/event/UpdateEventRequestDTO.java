package com.eb.eventsbridge.modules.core.dto.event;

import java.time.LocalDateTime;

import com.eb.eventsbridge.modules.core.entity.EventStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateEventRequestDTO {

    @NotNull
    private Long clubId;

    @NotNull
    private Long eventId;

    @NotBlank
    private String title;

    private String description;
    private String location;
    private Integer capacity;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

}
