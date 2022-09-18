package io.AlMaSm7.coworkingspace.services;

import io.AlMaSm7.coworkingspace.model.User;
import io.AlMaSm7.coworkingspace.repositories.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SignUp {
    private final UserRepo userRepo;

    public User register(User newUser){
        newUser.setReservations(null);
        if(userRepo.findAll().isEmpty()){
            newUser.setRole("admin");
        } else {
            newUser.setRole("mitglied");
        }
        try{
            userRepo.save(newUser);
            return newUser;
        }catch (Exception e){
            return null;
        }
    }
}
