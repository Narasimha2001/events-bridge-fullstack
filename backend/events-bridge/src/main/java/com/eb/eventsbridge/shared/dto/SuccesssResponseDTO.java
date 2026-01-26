package com.eb.eventsbridge.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccesssResponseDTO {
	
	private String message;
	private ResponseStatus status;

}
