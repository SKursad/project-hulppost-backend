package nl.novi.hulppost.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.hulppost.dto.UserDto;
import nl.novi.hulppost.exception.ResourceNotFoundException;
import nl.novi.hulppost.model.Account;
import nl.novi.hulppost.model.Password;
import nl.novi.hulppost.model.User;
import nl.novi.hulppost.repository.AccountRepository;
import nl.novi.hulppost.repository.UserRepository;
import nl.novi.hulppost.security.CustomUserDetailsService;
import nl.novi.hulppost.security.JwtAuthenticationEntryPoint;
import nl.novi.hulppost.security.JwtAuthenticationFilter;
import nl.novi.hulppost.security.JwtTokenProvider;
import nl.novi.hulppost.service.AccountService;
import nl.novi.hulppost.service.ReplyService;
import nl.novi.hulppost.service.RequestService;
import nl.novi.hulppost.service.UserService;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.validation.constraints.AssertTrue;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private AccountService accountService;

    @MockBean
    private UserService userService;

    @MockBean
    private ReplyService replyService;

    @MockBean
    private RequestService requestService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    public AccountRepository accountRepository;

    @MockBean
    public UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;


    @Test
    public void givenHelpSeekerObject_whenCreateHelpSeeker_thenReturnSavedHelpSeeker() throws Exception {

        // given - precondition or setup
        User user = User.builder()
                .username("Kurshad")
                .email("Kursad85@mail.com")
                .password("Test1234")
                .build();
        given(userService.registerHelpSeeker(any(UserDto.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when - action that's under test
        ResultActions response = mockMvc.perform(post("/auth/registration/helpSeeker")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        // then - verify output
        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.username",
                        is(user.getUsername())))
                .andExpect(jsonPath("$.email",
                        is(user.getEmail())))
                .andExpect(jsonPath("$.password",
                        is(user.getPassword())));

    }

    @Test
    public void givenVolunteerObject_whenCreateVolunteer_thenReturnSavedVolunteer() throws Exception {

        // given - precondition or setup
        User user = User.builder()
                .username("Kurshad")
                .email("Kursad85@mail.com")
                .password("Test1234")
                .build();
        given(userService.registerVolunteer(any(UserDto.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when - action that's under test
        ResultActions response = mockMvc.perform(post("/auth/registration/volunteer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        // then - verify output
        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.username",
                        is(user.getUsername())))
                .andExpect(jsonPath("$.email",
                        is(user.getEmail())))
                .andExpect(jsonPath("$.password",
                        is(user.getPassword())));

    }

    @Test
    public void givenAdminObject_whenCreateAdmin_thenReturnSavedAdmin() throws Exception {

        // given - precondition or setup
        User user = User.builder()
                .username("Kurshad")
                .email("Kursad85@mail.com")
                .password("Test1234")
                .build();
        given(userService.registerAdmin(any(UserDto.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when - action that's under test
        ResultActions response = mockMvc.perform(post("/auth/registration/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        // then - verify output
        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.username",
                        is(user.getUsername())))
                .andExpect(jsonPath("$.email",
                        is(user.getEmail())))
                .andExpect(jsonPath("$.password",
                        is(user.getPassword())));

    }
}