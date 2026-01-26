package com.eb.eventsbridge.modules.core.dto.club;


import java.time.LocalDateTime;

import com.eb.eventsbridge.modules.core.dto.common.UserSummaryDto;

public record ClubSummaryDto(
        Long id,
        String name,
        String description,
        String logoUrl,
        UserSummaryDto creator,
        int memberCount,
        LocalDateTime createdAt
) {}
