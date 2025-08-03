package org.ahavah.portal.dtos.user;

import lombok.Data;

@Data
public class UserDto {

    Long id;
    String name;
    String email;
    String division;
    String role;

}
