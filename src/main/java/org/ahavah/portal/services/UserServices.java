package org.ahavah.portal.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.ahavah.portal.dtos.user.*;
import org.ahavah.portal.entities.User;
import org.ahavah.portal.mappers.UserMapper;
import org.ahavah.portal.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServices {


    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    @Value("${default_pass}")
    String defaultPass;

    public UserDto createUser(CreateUserRequest createUserRequest) {

        var user = userMapper.toEntity(createUserRequest);
        if (user.getPassword() == null)
            user.setPassword(defaultPass);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
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


    public User currentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof Long userId) {

            return userRepository.findById(userId).orElse(null);
        }
        // Handle cases where the principal is not a Long, if necessary
        throw new IllegalStateException("User ID in security context is not a Long.");
    }

    public Map<String, String> updatePassword(UpdatePassRequest updatePassRequest){
        var user = currentUser();
        if (!passwordEncoder.matches(updatePassRequest.getOldPass(), user.getPassword())) {
            throw new IllegalArgumentException("Old password does not match");
        }

        user.setPassword(passwordEncoder.encode(updatePassRequest.getNewPass()));
        userRepository.save(user);
        return Map.of("status", "success");


    }

    public Map<String, String> addApiKey(AddApiKey addApiKey){
        var user = currentUser();
        user.setApiKey(addApiKey.getApiKey());
        userRepository.save(user);
        return Map.of("status", "success");
    }

}
