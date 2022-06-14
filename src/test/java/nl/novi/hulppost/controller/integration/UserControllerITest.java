package nl.novi.hulppost.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.hulppost.model.User;
import nl.novi.hulppost.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerITest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    public void givenUserObject_whenCreateUser_thenReturnSavedUser() throws Exception {

        // given - precondition or setup
        User volunteer = User.builder()
                .id(1L)
                .username("Test")
                .email("Test@gmail.com")
                .password("Test1234")
                .build();

        // when - action that's under test
        ResultActions response = mockMvc.perform(post("/auth/registration/volunteer").with(user(volunteer.getUsername()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(volunteer)));

        // then - verify the output
        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.username",
                        is(volunteer.getUsername())))
                .andExpect(jsonPath("$.email",
                        is(volunteer.getEmail())));
//                .andExpect(jsonPath("$.password",
//                        is(volunteer.getPassword())));

    }

    @Test
    public void givenListOfUsers_whenGetAllUsers_thenReturnUsersList() throws Exception {

        // given
        List<User> listOfUsers = new ArrayList<>();
        listOfUsers.add(User.builder()
                .username("Kursad")
                .email("Kurshda85@gmail.com")
                .password("Test1234A")
                .build());
        listOfUsers.add(User.builder()
                .username("Test")
                .email("Test@gmail.com")
                .password("1234ATest")
                .build());
        userRepository.saveAll(listOfUsers);

        // when
        ResultActions response = mockMvc.perform(get("/hulppost/users"));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfUsers.size())));

    }

    // positive scenario - valid user id
    @Test
    public void givenUserId_whenGetUserById_thenReturnUserObject() throws Exception {

        // given
        User user = User.builder()
                .username("Kursad")
                .email("Kurshad85@gmail.com")
                .password("Test1234")
                .build();
        userRepository.save(user);

        // when
        ResultActions response = mockMvc.perform(get("/hulppost/users/{userId}", user.getId()).with(user("Test")));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
//                .andExpect(jsonPath("$.password", is(user.getPassword())));

    }

    // negative scenario - valid user id
    @Test
    public void givenInvalidUserId_whenGetUserById_thenReturnEmpty() throws Exception {

        // given
        Long userId = 2L;
        User user = User.builder()
                .username("Salim")
                .email("Kurshad85@gmail.com")
                .password("Test1234A")
                .build();
        userRepository.save(user);

        // when
        ResultActions response = mockMvc.perform(get("/hulppost/users/{userId}", userId).with(user("Test")));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    public void givenUpdatedUser_whenUpdateUser_thenReturnUpdateUserObject() throws Exception {

        // given
        User savedUser = User.builder()
                .username("Kursad")
                .email("Kurshad85@gmail.com")
                .password("Test1234")
                .build();
        userRepository.save(savedUser);

        User updatedUser = User.builder()
//                .id(1L)
                .username("Salim")
                .email("Kurshad@live.nl")
                .password("Test1234")
                .build();

        // when
        ResultActions response = mockMvc.perform(put("/hulppost/users/{userId}", savedUser.getId()).with(user(savedUser.getUsername()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)));


        // then
        response.andExpect(status().isOk())
                .andDo(print())
//                .andExpect(jsonPath("$.id", is(updatedUser.getId())))
                .andExpect(jsonPath("$.username", is(updatedUser.getUsername())))
                .andExpect(jsonPath("$.email", is(updatedUser.getEmail())));
    }

    @Test
    public void givenUpdatedUser_whenUpdateUser_thenReturn404() throws Exception {

        // given
        Long userId = 2L;
        User savedUser = User.builder()
                .username("Kursad")
                .email("Kurshad85@gmail.com")
                .password("Test1234A")
                .build();
        userRepository.save(savedUser);

        User updatedUser = User.builder()
                .id(19L)
                .username("Salim")
                .email("Kurshad@live.nl")
                .password("Test1234B")
                .build();

        // when
        ResultActions response = mockMvc.perform(put("/hulppost/users/{userId}", userId).with(user(updatedUser.getUsername()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenUserId_whenDeleteUser_thenReturn200() throws Exception {

        // given
        User savedUser = User.builder()
                .username("Kursad")
                .email("Kurshad85@gmail.com")
                .password("Test1234A")
                .build();
        userRepository.save(savedUser);

        // when
        ResultActions response = mockMvc.perform(delete("/hulppost/users/{userId}", savedUser.getId()).with(user(savedUser.getUsername())));

        // then
        response.andExpect(status().isOk())
                .andDo(print());
    }
}


