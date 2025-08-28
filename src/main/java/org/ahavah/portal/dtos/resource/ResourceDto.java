package org.ahavah.portal.dtos.resource;

import lombok.Data;
import org.ahavah.portal.dtos.user.UserDto;

@Data
public class ResourceDto {
    private Long id;
    private String title;
    private String address;
    private UserDto user;

}
