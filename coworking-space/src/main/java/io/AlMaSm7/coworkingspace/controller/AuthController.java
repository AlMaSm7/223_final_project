package io.AlMaSm7.coworkingspace.controller;

import io.AlMaSm7.coworkingspace.model.TokenResponse;
import io.AlMaSm7.coworkingspace.model.User;
import io.AlMaSm7.coworkingspace.repositories.UserRepo;
import io.AlMaSm7.coworkingspace.security.JwtServiceHMAC;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtServiceHMAC jwtService;

    @Autowired
    private UserRepo memberRepository;

    @Operation(
            summary = "Get new token",
            operationId = "getToken",
            tags = {"Authorization"}
    )
    @PostMapping(value = "/token", produces = "application/json")
    public TokenResponse getToken(
            @Parameter(
                    description = "The grant type which will be used to get an new token",
                    required = true,
                    schema = @Schema(allowableValues = {"password", "refresh_token"})
            )
            @RequestParam(name = "grant_type", required = true)
                    String grantType,
            @Parameter(description = "If refresh_token is selected as grant type this field is needed")
            @RequestParam(name = "refresh_token", required = false)
                    String refreshToken,
            @Parameter(description = "If password is selected as grant type this field is needed", required = false)
            @RequestParam(name = "email", required = false)
                    String email,
            @Parameter(description = "If password is selected as grant type this field is needed", required = false)
            @RequestParam(name = "password", required = false)
                    String password) throws GeneralSecurityException, IOException {

        switch (grantType) {
            case "password" -> {
                val optionalMember = memberRepository.findByEmail(email);
                if (optionalMember.isEmpty()) {
                    throw new IllegalArgumentException("Username or password wrong");
                }

                if (!BCrypt.checkpw(password, optionalMember.get().getPassword())) {
                    throw new IllegalArgumentException("Username or password wrong");
                }

                val member = optionalMember.get();

                val id = UUID.randomUUID().toString();
                val scopes = new ArrayList<String>();

                if (Objects.equals(member.getRole(), "admin")) {
                    scopes.add("ADMIN");
                }

                val newAccessToken = jwtService.createNewJWT(id, String.valueOf(member.getId()), member.getEmail(), scopes);
                val newRefreshToken = jwtService.createNewJWTRefresh(id, String.valueOf(member.getId()));

                return new TokenResponse(newAccessToken, newRefreshToken, "Bearer", LocalDateTime.now().plusDays(14).toEpochSecond(ZoneOffset.UTC), LocalDateTime.now().plusDays(1).toEpochSecond(ZoneOffset.UTC));
            }
            case "refresh_token" -> {
                val jwt = jwtService.verifyJwt(refreshToken, false);

                val optionalMember = memberRepository.findById(Long.valueOf(
                        String.valueOf(jwt.getClaim("user_id"))));
                if (optionalMember.isEmpty()) {
                    throw new IllegalArgumentException("Invalid refresh token");
                }

                val member = optionalMember.get();

                val id = UUID.randomUUID().toString();
                val scopes = new ArrayList<String>();

                if (member.getRole() == "admin") {
                    scopes.add("ADMIN");
                }

                val newAccessToken = jwtService.createNewJWT(id, String.valueOf(member.getId()), member.getEmail(), scopes);
                val newRefreshToken = jwtService.createNewJWTRefresh(id, String.valueOf(member.getId()));

                return new TokenResponse(newAccessToken, newRefreshToken, "Bearer", LocalDateTime.now().plusDays(14).toEpochSecond(ZoneOffset.UTC), LocalDateTime.now().plusDays(1).toEpochSecond(ZoneOffset.UTC));
            }
            default -> throw new IllegalArgumentException("Not supported grant type: " + grantType);
        }
    }

    @Operation(
            summary = "Register user",
            operationId = "register",
            tags = {"Authorization"}
    )
    @PostMapping(value = "/register", produces = "application/json")
    public TokenResponse register(
            @Parameter(description = "Username / E-Mail", required = true)
            @RequestParam(name = "username", required = true)
                    String username,
            @Parameter(description = "Password", required = true)
            @RequestParam(name = "password", required = true)
                    String password,
            @Parameter(description = "firstname", required = true)
            @RequestParam(name = "firstname", required = true)
                    String firstname,
            @Parameter(description = "lastname", required = true)
            @RequestParam(name = "lastname", required = true)
                    String lastname

    ) throws GeneralSecurityException, IOException {
        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
        val newMember = new User(firstname, lastname, username, passwordHash);
        memberRepository.save(newMember);

        return getToken("password", "", username, password);
    }
}