package org.ahavah.portal.dtos.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AddApiKey {
    @NotEmpty
    private String apiKey;
}
