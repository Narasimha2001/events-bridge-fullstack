package com.eb.eventsbridge.modules.auth.dto.register;

import lombok.Data;

@Data
public class RegisterResponseDTO {

    private String id;

    private String fullName;

    private String email;
    
    private String roleName;

}
