package com.example.jo1ntask.controller;

import com.example.jo1ntask.domain.User;
import com.example.jo1ntask.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private final User user1 =
            new User("name1", "lastname1", "login1", "pass1", "description1");

    private final User user2 =
            new User("name2", "lastname2", "login2", "pass2", "description2");

    @BeforeAll
    public void setUp() {
        userRepository.save(user1);
        userRepository.save(user2);
    }

    @AfterAll
    public void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    public void allUsersAreReturned() {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<User> users = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {});

        assertEquals(2, users.size());
    }

    @Test
    @DisplayName("First user can be found")
    @SneakyThrows
    public void userCanBeFound() {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        User foundUser = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {});

        assertEquals(user1, foundUser);
    }

    @Test
    @SneakyThrows
    public void correctUserIsCreated() {
        User user3 = new User("name3", "lastname3", "login3", "pass3", "description3");

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user3)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("User with no password will return info about missing field")
    @SneakyThrows
    public void incorrectUserReturnsProblemField() {
        User user4 = new User("name4", "lastname4", "login4", null, "description4");
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user4)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", Is.is("must not be blank")));
    }

    @Test
    @SneakyThrows
    public void userCanBeDeleted() {
        User user = userRepository.findById(2L).orElse(null);
        assertNotNull(user);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        user = userRepository.findById(2L).orElse(null);

        assertNull(user);
    }
}
