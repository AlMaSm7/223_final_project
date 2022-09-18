package io.AlMaSm7.coworkingspace.controller;

import io.AlMaSm7.coworkingspace.model.User;
import io.AlMaSm7.coworkingspace.repositories.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    private final UserRepo userRepo;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(){
        List<User> users = userRepo.findAll();
        if (users.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok().body(users);
    }

    @DeleteMapping("/users")
    public ResponseEntity<?> delUser(@RequestBody User user){
        Optional<User> user1 = userRepo.findById(user.getId());
        if (user1.isEmpty()) return ResponseEntity.notFound().build();
        // Delete user
        try{
            userRepo.delete(user1.get());
            return ResponseEntity.ok().body(user1.get().getEmail() + "User deleted successfully");
        }catch (Exception e){
            return ResponseEntity.internalServerError().body("There was an issue deleting the user");
        }
    }

    @PatchMapping("/users")
    public ResponseEntity<?> updateUser(@RequestBody User user){
        try{
            Optional<User> userToUpdate = userRepo.findById(user.getId());
            User updatedUser = userToUpdate.get();
            if(updatedUser == null) return ResponseEntity.notFound().build();
            //Add edited values to User found in db
            updatedUser.setEmail(user.getEmail());
            updatedUser.setLastname(user.getLastname());
            updatedUser.setReservations(user.getReservations());
            updatedUser.setFirstname(user.getFirstname());
            updatedUser.setRole(user.getRole());
            userRepo.save(updatedUser);
            return ResponseEntity.ok().body(user.getEmail() + "User updated successfully");
        }catch (Exception e){
            return ResponseEntity.internalServerError().body("There was an issue updating the user");
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable long id){
        try{
            return ResponseEntity.ok().body(userRepo.findById(id));
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/users")
    public ResponseEntity<?> registerUserAsAdmin(@RequestBody User user){
        try{
            userRepo.save(user);
            return ResponseEntity.ok().body(user);
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error while saving user");
        }
    }
}
