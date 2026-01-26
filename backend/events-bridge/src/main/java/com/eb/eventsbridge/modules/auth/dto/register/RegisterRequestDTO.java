package com.eb.eventsbridge.modules.auth.dto.register;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    @NotBlank(message = "Name is Required")
    private String fullName;

    @NotBlank(message = "Email is Required")
    @Email()
    private String email;

    @NotBlank(message = "Password is Required")
    @Size(message = "Password length should be between 8 to 16", min = 8, max = 16)
    private String password;
    
    @NotBlank(message="Role is required")
    private String roleName;

}
