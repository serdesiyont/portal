package org.ahavah.portal.dtos.otp;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class VerifyOTPRequest {
    @Email
    private String email;
    @NotBlank
    private String purpose;
    @NotEmpty
    private Integer code;
}
