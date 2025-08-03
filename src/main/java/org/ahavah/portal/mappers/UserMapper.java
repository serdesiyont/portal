package org.ahavah.portal.mappers;

import org.ahavah.portal.dtos.user.CreateUserRequest;
import org.ahavah.portal.dtos.user.UserDto;
import org.ahavah.portal.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userDto(User user);
    User toEntity(CreateUserRequest createUserRequest);

}
