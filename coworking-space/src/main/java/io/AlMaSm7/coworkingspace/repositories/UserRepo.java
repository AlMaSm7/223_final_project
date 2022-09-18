package io.AlMaSm7.coworkingspace.repositories;

import io.AlMaSm7.coworkingspace.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository {
    Optional<User> findByEmail(String email);
}
