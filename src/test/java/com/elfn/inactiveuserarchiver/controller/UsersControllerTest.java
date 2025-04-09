package com.elfn.inactiveuserarchiver.controller;

import com.elfn.inactiveuserarchiver.dao.ArchivedUserRepository;
import com.elfn.inactiveuserarchiver.dao.UserRepository;
import com.elfn.inactiveuserarchiver.model.ArchivedUser;
import com.elfn.inactiveuserarchiver.model.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


/**
 * @Author: Elimane
 */
@SpringBootTest
@AutoConfigureMockMvc
public class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArchivedUserRepository archivedUserRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        archivedUserRepository.deleteAll();

        Users user1 = new Users(null, "carla", "carla@example.com", LocalDate.now().minusDays(10));
        Users user2 = new Users(null, "emilie", "emilie@example.com", LocalDate.now().minusDays(5));
        userRepository.saveAll(List.of(user1, user2));

        archivedUserRepository.save(new ArchivedUser(null, "hugo", "hugo@example.com", LocalDate.now().minusDays(100)));
    }

    @Test
    void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetAllArchivedUsers() throws Exception {
        mockMvc.perform(get("/api/users/archived").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
