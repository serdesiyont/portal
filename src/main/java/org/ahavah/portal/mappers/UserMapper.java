package org.ahavah.portal.mappers;

import org.ahavah.portal.dtos.user.CreateUserRequest;
import org.ahavah.portal.dtos.user.UserDto;
import org.ahavah.portal.entities.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {




    User toEntity(CreateUserRequest createUserRequest);
    UserDto userDto(User user);
    List<UserDto> userDtos(List<User> users);

}
