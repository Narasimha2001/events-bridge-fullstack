package com.eb.eventsbridge.modules.core.dto.feedback;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SubmitFeedbackRequestDTO {
    @NotNull @Min(1) @Max(5)
    private Integer rating;
    
    @NotBlank
    @Size(max = 500)
    private String comment;
}