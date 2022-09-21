package io.AlMaSm7.coworkingspace;

import io.AlMaSm7.coworkingspace.model.User;
import io.AlMaSm7.coworkingspace.repositories.UserRepo;
import io.AlMaSm7.coworkingspace.security.JwtServiceHMAC;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthController {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtServiceHMAC jwtService;
    @Autowired
    private UserRepo repo;

    private String accessToken;

    @BeforeEach
    public void init() {
        accessToken = jwtService.createNewJWT(UUID.randomUUID().toString(), "1", "email@gmail.com", List.of("ADMIN"));
    }

    @Test
    @Description("Test if register works")
    public void testRegistration() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("password", Collections.singletonList("d@gmail.com"));
        params.put("username", Collections.singletonList("fassdfasfjasfk@gmail.com"));
        params.put("firstname", Collections.singletonList("s"));
        params.put("lastname", Collections.singletonList("d"));

        mockMvc.perform(post("/auth/register").params(params))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        Optional<User> deleteUser = repo.findByEmail("fassdfasfjasfk@gmail.com");

        if (deleteUser.isPresent()) {
            repo.delete(deleteUser.get());
        }
    }

    @Test
    @Description("Test if register bad Request works with wrong param")
    public void badRequest() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("password", Collections.singletonList("d@gmail.com"));
        params.put("EMAIL", Collections.singletonList("fassdfasfjasfk@gmail.com"));
        params.put("firstname", Collections.singletonList("s"));
        params.put("lastname", Collections.singletonList("d"));

        mockMvc.perform(post("/auth/register").params(params))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();
    }
}
