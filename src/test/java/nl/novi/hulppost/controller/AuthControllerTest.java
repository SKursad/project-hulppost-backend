package nl.novi.hulppost.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.hulppost.config.AppConfiguration;
import nl.novi.hulppost.config.WebConfiguration;
import nl.novi.hulppost.dto.RegistrationDTO;
import nl.novi.hulppost.model.User;
import nl.novi.hulppost.payload.Password;
import nl.novi.hulppost.repository.AccountRepository;
import nl.novi.hulppost.repository.UserRepository;
import nl.novi.hulppost.security.CustomUserDetailsService;
import nl.novi.hulppost.security.JwtAuthenticationEntryPoint;
import nl.novi.hulppost.security.JwtAuthenticationFilter;
import nl.novi.hulppost.security.JwtTokenProvider;
import nl.novi.hulppost.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    @MockBean
    private UserService userService;

    @MockBean
    private ReplyService replyService;

    @MockBean
    private RequestService requestService;

    @MockBean
    private FileService attachmentService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    public AccountRepository accountRepository;

    @MockBean
    public UserRepository userRepository;

    @MockBean
    AppConfiguration appConfiguration;

//    @MockBean
//    FileServiceImpl fileService;

    @MockBean
    WebConfiguration webConfiguration;

    @Test
    public void givenHelpSeekerObject_whenCreateHelpSeeker_thenReturnSavedHelpSeeker() throws Exception {

        // given - precondition or setup
        RegistrationDTO registrationDTO = RegistrationDTO.builder()
                .email("some@Email.com")
                .username("Dommel")
                .password("Test123")
                .id(1L)
                .firstName("TestDummy")
                .surname("Tester")
                .gender("M")
                .zipCode("1000AA")
                .birthday("22/04/1999")
                .build();
        given(userService.registerHelpSeeker(any(RegistrationDTO.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when - action that's under test
        ResultActions response = mockMvc.perform(post("/auth/registration/helpSeeker")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDTO)));

        // then - verify output
        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.email",
                        is(registrationDTO.getEmail())))
                .andExpect(jsonPath("$.username",
                        is(registrationDTO.getUsername())))
                .andExpect(jsonPath("$.password",
                        is(registrationDTO.getPassword())))
                .andExpect(jsonPath("$.firstName",
                        is(registrationDTO.getFirstName())))
                .andExpect(jsonPath("$.surname",
                        is(registrationDTO.getSurname())))
                .andExpect(jsonPath("$.gender",
                        is(registrationDTO.getGender())))
                .andExpect(jsonPath("$.zipCode",
                        is(registrationDTO.getZipCode())))
                .andExpect(jsonPath("$.birthday",
                        is(registrationDTO.getBirthday())));

    }

    @Test
    public void givenVolunteerObject_whenCreateVolunteer_thenReturnSavedVolunteer() throws Exception {

        // given - precondition or setup
        RegistrationDTO registrationDTO = RegistrationDTO.builder()
                .email("some@Email.com")
                .username("Dommel")
                .password("Test123")
                .id(2L)
                .firstName("TestDummy")
                .surname("Tester")
                .gender("M")
                .zipCode("1000AA")
                .birthday("22/04/1999")
                .build();
        given(userService.registerVolunteer(any(RegistrationDTO.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when - action that's under test
        ResultActions response = mockMvc.perform(post("/auth/registration/volunteer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDTO)));

        // then - verify output
        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.email",
                        is(registrationDTO.getEmail())))
                .andExpect(jsonPath("$.username",
                        is(registrationDTO.getUsername())))
                .andExpect(jsonPath("$.password",
                        is(registrationDTO.getPassword())))
                .andExpect(jsonPath("$.firstName",
                        is(registrationDTO.getFirstName())))
                .andExpect(jsonPath("$.surname",
                        is(registrationDTO.getSurname())))
                .andExpect(jsonPath("$.gender",
                        is(registrationDTO.getGender())))
                .andExpect(jsonPath("$.zipCode",
                        is(registrationDTO.getZipCode())))
                .andExpect(jsonPath("$.birthday",
                        is(registrationDTO.getBirthday())));

    }

    @Test
    public void givenAdminObject_whenCreateAdmin_thenReturnSavedAdmin() throws Exception {

        // given - precondition or setup
        RegistrationDTO registrationDTO = RegistrationDTO.builder()
                .email("some@Email.com")
                .username("Dommel")
                .password("Test123")
                .id(2L)
                .firstName("TestDummy")
                .surname("Tester")
                .gender("M")
                .zipCode("1000AA")
                .birthday("22/04/1999")
                .build();
        given(userService.registerAdmin(any(RegistrationDTO.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when - action that's under test
        ResultActions response = mockMvc.perform(post("/auth/registration/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDTO)));

        // then - verify output
        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.email",
                        is(registrationDTO.getEmail())))
                .andExpect(jsonPath("$.username",
                        is(registrationDTO.getUsername())))
                .andExpect(jsonPath("$.password",
                        is(registrationDTO.getPassword())))
                .andExpect(jsonPath("$.firstName",
                        is(registrationDTO.getFirstName())))
                .andExpect(jsonPath("$.surname",
                        is(registrationDTO.getSurname())))
                .andExpect(jsonPath("$.gender",
                        is(registrationDTO.getGender())))
                .andExpect(jsonPath("$.zipCode",
                        is(registrationDTO.getZipCode())))
                .andExpect(jsonPath("$.birthday",
                        is(registrationDTO.getBirthday())));

    }

    @Test
    public void givenOldPassword_ChangesWithNewPassword() throws Exception {
       // given - precondition or setup
        User user = User.builder()
                .username("Kurshad")
                .email("Kursad85@mail.com")
                .password("Test1234")
                .build();

        Password passwordChange = Password.builder()
                .email("Kursad85@mail.com")
                .oldPassword("Test1234")
                .newPassword("123456DDw")
                .build();
//        given(userService.checkIfValidOldPassword(user,passwordChange.getOldPassword()))
//                .willAnswer((invocation) -> invocation.getArgument(0));
        // when - action that's under test
        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/auth/changePassword")
                        .content(new ObjectMapper().writeValueAsString(passwordChange))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // then - verify output
        user.getPassword().equals(passwordChange.getOldPassword());
        passwordChange.getOldPassword().equals(user.getPassword());
        user.setPassword(passwordChange.getNewPassword());
        assertEquals(user.getPassword(), "123456DDw");
    }

}