package io.AlMaSm7.coworkingspace;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.AlMaSm7.coworkingspace.model.Reservation;
import io.AlMaSm7.coworkingspace.model.User;
import io.AlMaSm7.coworkingspace.security.JwtServiceHMAC;
import jdk.jfr.Description;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtServiceHMAC jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private String accessToken;

    @BeforeEach
    public void init() {
        accessToken = jwtService.createNewJWT(UUID.randomUUID().toString(), "1", "email@gmail.com", List.of("ADMIN"));
    }

    @Test
    @Description("test if users are found according to Testdata")
    public void testUserEndpoint() throws Exception {
        val res = mockMvc.perform(get("/api/users").header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        List<User> users = objectMapper.readValue(res.getResponse().getContentAsString(), new TypeReference<>() {});
        assertEquals(users.size(), 1);
    }

    @Test
    @Description("test if users are found according to Testdata")
    public void testUserIdEndpoint() throws Exception {
        val res = mockMvc.perform(get("/api/users/1").header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
      User users = objectMapper.readValue(res.getResponse().getContentAsString(), new TypeReference<>() {});
        assertEquals(users.getId(), 1);
    }

    @Test
    @Description("test if users are found according to Testdata")
    public void testUserIdErrorEndpoint() throws Exception {
        mockMvc.perform(get("/api/users/895324").header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
    }
}
