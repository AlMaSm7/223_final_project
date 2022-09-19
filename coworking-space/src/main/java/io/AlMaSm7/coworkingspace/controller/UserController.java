package io.AlMaSm7.coworkingspace.controller;

import io.AlMaSm7.coworkingspace.model.User;
import io.AlMaSm7.coworkingspace.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
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

    @DeleteMapping("/users/{id}")
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

    @PutMapping("/users")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        ResponseEntity res;
        try {
            if (userService.updateUser(user) != null) {
                res = ResponseEntity.notFound().build();
            } else {
                res = ResponseEntity.ok().body(user.getEmail() + "User updated successfully");
            }
            return res;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("There was an issue updating the user");
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable long id) {
        try {
            return ResponseEntity.ok().body(userService.getUserById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/users")
    public ResponseEntity<?> registerUserAsAdmin(@RequestBody User user) {
        try {
            userService.createNewUser(user);
            return ResponseEntity.ok().body(user);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error while saving user");
        }
    }
}
