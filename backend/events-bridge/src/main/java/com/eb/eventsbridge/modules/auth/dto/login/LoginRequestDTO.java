package com.eb.eventsbridge.modules.auth.dto.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotBlank(message = "Email is Required")
    @Email()
    private String email;

    @NotBlank(message = "password is required")
    @Size(message = "Password length should be between 8 to 16", min = 8, max = 16)
    private String password;
}
