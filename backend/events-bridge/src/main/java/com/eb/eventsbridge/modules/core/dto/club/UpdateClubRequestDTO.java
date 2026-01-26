package com.eb.eventsbridge.modules.core.dto.club;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateClubRequestDTO(
		
		@NotNull()
		Long clubId,
		
        @Size(min = 3, max = 100)
        String name,

        @Size(max = 500)
        String description,

        String logoUrl
) {}
