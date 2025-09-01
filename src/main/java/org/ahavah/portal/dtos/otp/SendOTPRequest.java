package org.ahavah.portal.dtos.otp;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SendOTPRequest {
    @NotBlank
    private String name;
    @Email
    private String email;
    @NotBlank
    private String purpose;
}
