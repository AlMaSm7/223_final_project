package io.AlMaSm7.coworkingspace.controller;

import io.AlMaSm7.coworkingspace.model.User;
import io.AlMaSm7.coworkingspace.security.JWTUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
//@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MainController {

    /*private final AuthenticationManager authManager;
    private final JWTUtil jwtUtil;*/

    @GetMapping("/api/hello")
    public String hello() {
        System.out.println("here");
        return "hello world";
    }

    /*@PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        System.out.println("entered");
        try {
            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
            System.out.println("here");
            authManager.authenticate(authInputToken);

            String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

            return (ResponseEntity<?>) Collections.singletonMap("jwt-token", token);
        } catch (AuthenticationException authExc) {
            throw new RuntimeException("Invalid Login Credentials");
        }
    }*/

}

