package io.AlMaSm7.coworkingspace.controller;

import io.AlMaSm7.coworkingspace.model.User;
import io.AlMaSm7.coworkingspace.services.SignUp;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class MainController {

    private final SignUp signUp;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@RequestBody User newUser) {
        log.info("Registering user....");
        User res = signUp.register(newUser);
        if (res == null) {
            return ResponseEntity.internalServerError().body("The user couldn't be uploaded due to the email not being unique");
        } else {
            return ResponseEntity.ok().body("Welcome: " + res.getFirstname() + "!");
        }
    }
}
