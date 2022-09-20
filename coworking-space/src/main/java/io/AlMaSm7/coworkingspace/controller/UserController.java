package io.AlMaSm7.coworkingspace.controller;

import io.AlMaSm7.coworkingspace.model.User;
import io.AlMaSm7.coworkingspace.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.getUsers();
        ResponseEntity res;
        if (users.isEmpty()) {
            res = ResponseEntity.noContent().build();
        } else {
            res = ResponseEntity.ok().body(users);
        }
        return res;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delUser(@PathVariable long id) {
        // Delete user
        ResponseEntity res;
        try {
            User user1 = userService.deleteUser(id);
            if (user1 == null) {
                res = ResponseEntity.notFound().build();
            } else {
                res = ResponseEntity.ok().body(user1.getEmail() + "User deleted successfully");
            }
            return res;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("There was an issue deleting the user");
        }
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        ResponseEntity res;
        try {
            if (userService.updateUser(user) != null) {
                res = ResponseEntity.notFound().build();
            } else {
                res = ResponseEntity.ok().body(user.getEmail() + "User updated successfully");
            }
            return res;
        } catch (HttpMessageNotWritableException e) {
            return ResponseEntity.internalServerError().body("There was an issue updating the user");
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserById(@PathVariable long id) {
        try {
            ResponseEntity res;
            if (userService.getUserById(id).isEmpty()){
                res = ResponseEntity.notFound().build();
            } else {
                res = ResponseEntity.ok().body(userService.getUserById(id));
            }
            return res;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerUserAsAdmin(@RequestBody User user) {
        try {
            userService.createNewUser(user);
            return ResponseEntity.ok().body(user);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error while saving user");
        }
    }
}
