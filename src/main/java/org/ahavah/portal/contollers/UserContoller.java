package org.ahavah.portal.contollers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.ahavah.portal.dtos.user.CreateUserRequest;
import org.ahavah.portal.dtos.user.UpdateUserRequest;
import org.ahavah.portal.dtos.user.UserDto;
import org.ahavah.portal.services.UserServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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


}
