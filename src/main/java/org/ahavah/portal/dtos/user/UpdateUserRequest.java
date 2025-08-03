package org.ahavah.portal.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.ahavah.portal.validators.DivisionSubset;
import org.ahavah.portal.validators.RoleSubset;

@Data
public class UpdateUserRequest {

    @NotBlank
    @Size(min =3, max = 126)
    String name;
    @Email
    String email;
    @DivisionSubset
    String division;
    @RoleSubset
    String role;
}
