package org.ahavah.portal.contollers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.ahavah.portal.dtos.user.*;
import org.ahavah.portal.services.UserServices;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserContoller {

    private final UserServices userServices;

    @GetMapping
    public List<UserDto> getUsers() {
        return this.userServices.getUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        var user = this.userServices.getUser(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(
          @Valid @RequestBody CreateUserRequest createUserRequest){
        var newUser = this.userServices.createUser(createUserRequest);
        return ResponseEntity.ok(newUser);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        this.userServices.deleteUser(id);
        return ResponseEntity.noContent().build();

}

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@Valid @PathVariable Long id, @RequestBody UpdateUserRequest updateUserRequest) {
        var user = this.userServices.updateUser(id, updateUserRequest);
        return ResponseEntity.ok(user);

    }

    @PreAuthorize("hasAnyRole('MENTOR', 'STUDENT', 'ADMIN')")
    @PostMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(
            @RequestBody UpdatePassRequest  updatePassRequest) {
        var change = this.userServices.updatePassword(updatePassRequest);
        return ResponseEntity.ok(change);
    }

    @PreAuthorize("hasAnyRole('MENTOR', 'STUDENT', 'ADMIN')")
    @PostMapping("/add-api")
    public ResponseEntity<Map<String, String>> addApiKey(
            @Valid @RequestBody AddApiKey addApiKey){
        var change = this.userServices.addApiKey(addApiKey);
        return ResponseEntity.ok(change);
    }





}
