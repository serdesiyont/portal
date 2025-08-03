package org.ahavah.portal.services;

import lombok.AllArgsConstructor;
import org.ahavah.portal.dtos.user.CreateUserRequest;
import org.ahavah.portal.dtos.user.UpdateUserRequest;
import org.ahavah.portal.dtos.user.UserDto;
import org.ahavah.portal.mappers.UserMapper;
import org.ahavah.portal.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServices {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserDto createUser(CreateUserRequest createUserRequest) {
        var user = userMapper.toEntity(createUserRequest);
        user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        userRepository.save(user);
        return userMapper.userDto(user);
    }

    public  UserDto updateUser(Long id, UpdateUserRequest updateUserRequest) {
        var user = userRepository.findById(id)
                .orElse(null);
        if (user == null) {
            return null; // or throw an exception
        }

        user.setName(updateUserRequest.getName());
        user.setEmail(updateUserRequest.getEmail());
        user.setDivision(updateUserRequest.getDivision());
        user.setRole(updateUserRequest.getRole());
        userRepository.save(user);
        return userMapper.userDto(user);
    }

    public UserDto getUser(Long id) {
        var user = userRepository.findById(id)
                .orElse(null);
        if (user == null) {
            return null; // or throw an exception
        }
        return userMapper.userDto(user);
    }

    public List<UserDto> getUsers() {
        var users = userRepository.findAll();
        return users.stream()
                .map(userMapper::userDto)
                .toList();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
