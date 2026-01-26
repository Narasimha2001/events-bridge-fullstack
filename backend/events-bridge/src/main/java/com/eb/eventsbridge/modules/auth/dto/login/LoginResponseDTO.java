package com.eb.eventsbridge.modules.auth.dto.login;

import com.eb.eventsbridge.modules.core.dto.common.UserSummaryDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    
    @JsonIgnore                // <--- HIDDEN from Front end JSON, we can use it in java files
    private String refreshToken;
    
    private UserSummaryDto user;
   

}
