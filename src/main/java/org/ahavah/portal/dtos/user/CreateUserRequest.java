package org.ahavah.portal.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.ahavah.portal.validators.DivisionSubset;
import org.ahavah.portal.validators.RoleSubset;


@Data
public class CreateUserRequest {
    @NotBlank
    @Size(min =3, max = 126)
    String name;

    @NotBlank
    @Email
    String email;


    String password; // default is defaultPassword

    @NotBlank
    @DivisionSubset
    String division;



    String role; // default is student


    String phone_num;

    @NotBlank
    String gender;

    String additional;


}
