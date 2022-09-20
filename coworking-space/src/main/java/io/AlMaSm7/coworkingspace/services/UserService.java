package io.AlMaSm7.coworkingspace.services;

import io.AlMaSm7.coworkingspace.model.Reservation;
import io.AlMaSm7.coworkingspace.model.User;
import io.AlMaSm7.coworkingspace.repositories.ReservationRepo;
import io.AlMaSm7.coworkingspace.repositories.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final UserRepo userRepo;
    private final ReservationRepo resReop;

    @Transactional
    public List<User> getUsers() {
        List<User> users = userRepo.findAll();
        return users;
    }

    @Transactional
    public Optional<User> getUserById(long id) {
        Optional<User> user = userRepo.findById(id);
        return user;
    }

    @Transactional
    public User createNewUser(User user) {
        userRepo.save(user);
        return user;
    }

    @Transactional
    public User updateUser(User user) {
        Optional<User> userToUpdate = userRepo.findById(user.getId());
        User updatedUser;
        if (userToUpdate.isPresent()){
            updatedUser = userToUpdate.get();
            //Add edited values to User found in db
            updatedUser.setEmail(user.getEmail());
            updatedUser.setLastname(user.getLastname());
            updatedUser.setReservations(user.getReservations());
            updatedUser.setFirstname(user.getFirstname());
            updatedUser.setRole(user.getRole());
            userRepo.save(updatedUser);

        } else {
            updatedUser = null;
        }
        return updatedUser;
    }

    @Transactional
    public User deleteUser(long id) {
        Optional<User> user1 = userRepo.findById(id);
        if (user1.isPresent()){
            Reservation resUser = resReop.findByUserId(user1.get().getId());
            if(resUser != null ){
                resReop.delete(resUser);
            }
            userRepo.delete(user1.get());
        }
        return user1.get();
    }
}
