package org.ahavah.portal.dtos.auth;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class LoginDto {

    @NotBlank(message = "Email required")
    @Email
    private String email;

    @NotBlank(message = "Password required")
    @Length(min = 6, max = 16)
    private String password;
}
