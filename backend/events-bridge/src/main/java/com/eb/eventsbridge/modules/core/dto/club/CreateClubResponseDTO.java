package com.eb.eventsbridge.modules.core.dto.club;

import java.util.List;

import lombok.Data;

@Data
public class CreateClubResponseDTO {
	
	Long id;
    String name;
    String description;
    String logoUrl;
    String creatorId;
    List<ClubMemberDto> members;

}
