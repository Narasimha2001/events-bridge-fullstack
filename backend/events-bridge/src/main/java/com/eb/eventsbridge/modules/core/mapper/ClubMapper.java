package com.eb.eventsbridge.modules.core.mapper;

import com.eb.eventsbridge.modules.core.dto.club.ClubSummaryDto;
import com.eb.eventsbridge.modules.core.dto.common.UserSummaryDto;
import com.eb.eventsbridge.modules.core.entity.Club;

public class ClubMapper {

    private ClubMapper() {}

    public static ClubSummaryDto toSummaryDto(Club club) {
        return new ClubSummaryDto(
                club.getId(),
                club.getName(),
                club.getDescription(),
                club.getLogoUrl(),
                new UserSummaryDto(
                        club.getCreator().getId(),
                        club.getCreator().getFullName(),
                        club.getCreator().getEmail(),
                        club.getCreator().getRole().getName()
                ),
                club.getMembers().size(), // .size() doesn't load collection fully
                club.getCreatedAt()
        );
    }
}
