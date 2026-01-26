package com.eb.eventsbridge.modules.core.dto.club;

import java.util.List;

import com.eb.eventsbridge.modules.core.entity.ClubMember;
import com.eb.eventsbridge.modules.core.entity.Event;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateClubRequestDTO {
	@NotBlank(message="Club name is required")
	private String name;
	
	private String description;
	
	private String logoUrl;
	
	private List<ClubMember> members;
	
	private List<Event> events;
	
}
