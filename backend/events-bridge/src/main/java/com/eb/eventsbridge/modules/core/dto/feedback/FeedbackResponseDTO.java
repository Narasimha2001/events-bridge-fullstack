package com.eb.eventsbridge.modules.core.dto.feedback;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedbackResponseDTO {
    private Long feedbackId;
    private String userName;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}