package com.eb.eventsbridge.modules.core.dto.club;

import java.time.LocalDateTime;

import com.eb.eventsbridge.modules.core.entity.ClubRole;

import lombok.Data;

@Data
public class ClubMemberDto {
	Long userId;
    String userName;
    ClubRole role;
    LocalDateTime joinedAt;
}
