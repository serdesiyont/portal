package org.ahavah.portal.dtos.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdatePassRequest {
    @NotEmpty
    private String oldPass;
    @NotEmpty
    private String newPass;
}
