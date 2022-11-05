package nl.novi.hulppost.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.hulppost.config.AppConfiguration;
import nl.novi.hulppost.config.WebConfiguration;
import nl.novi.hulppost.dto.RegistrationDTO;
import nl.novi.hulppost.model.Account;
import nl.novi.hulppost.model.User;
import nl.novi.hulppost.repository.AccountRepository;
import nl.novi.hulppost.repository.AttachmentRepository;
import nl.novi.hulppost.repository.RoleRepository;
import nl.novi.hulppost.repository.UserRepository;
import nl.novi.hulppost.security.MethodLevelSecurityService;
import nl.novi.hulppost.service.serviceImpl.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AccountControllerITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    AttachmentRepository attachmentRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    MethodLevelSecurityService methodLevelSecurityService;

    @MockBean
    AppConfiguration appConfiguration;

    @MockBean
    FileServiceImpl fileService;

    @MockBean
    WebConfiguration webConfiguration;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        accountRepository.deleteAll();
    }

//    @Test
//    private void mockAuthentication()  throws Exception {
//        Authentication auth = mock(Authentication.class);
//
//        when(auth.getPrincipal()).thenReturn(user("User"));
//
//        SecurityContext securityContext = mock(SecurityContext.class);
//        when(securityContext.getAuthentication()).thenReturn(auth);
//        SecurityContextHolder.setContext(securityContext);
//    }


    @Test
//    @WithMockUser (roles = "ADMIN")
    public void givenAccountObject_whenCreateAccount_thenReturnSavedAccount() throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        // given - precondition or setup
        RegistrationDTO helpSeeker = RegistrationDTO.builder()
                .email("some@Email.com")
                .username("Dommel")
                .password("Test123")
                .id(20L)
                .firstName("DummyPerson")
                .surname("Tester")
                .gender("M")
                .zipCode("1000AA")
                .birthday(new Date(1999-4-22))
                .build();


        // when - action that's under test
        ResultActions response = mockMvc.perform(post("/api/v1/auth/registration/helpSeeker").with(user("Test"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(helpSeeker)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName",
                        is(helpSeeker.getFirstName())))
                .andExpect(jsonPath("$.surname",
                        is(helpSeeker.getSurname())))
                .andExpect(jsonPath("$.gender",
                        is(helpSeeker.getGender())))
                .andExpect(jsonPath("$.zipCode",
                        is(helpSeeker.getZipCode())))
                .andExpect(jsonPath("$.birthday",
                        is(df.format(helpSeeker.getBirthday()))));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void givenListOfAccounts_whenGetAllAccounts_thenReturnAccountsList() throws Exception {

        // given
        List<Account> listOfAccounts = new ArrayList<>();
        listOfAccounts.add(Account.builder()
                .id(10L)
                .firstName("Kursad")
                .surname("Dursun")
                .zipCode("1234AA")
                .build());

        listOfAccounts.add(Account.builder()
                .id(11L)
                .firstName("DummyPerson")
                .surname("Tester")
                .zipCode("1454AA")
                .build());
        accountRepository.saveAll(listOfAccounts);

        // when
        ResultActions response = mockMvc.perform(get("/api/v1/accounts"));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfAccounts.size())));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void givenAccountId_whenGetAccountById_thenReturnAccountObject() throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        // given
        Account account = Account.builder()
                .firstName("Kursad")
                .surname("Dursun")
                .gender("M")
                .birthday(new Date(1985-2-24))
                .zipCode("1000AA")
                .build();
        accountRepository.save(account);

        // when
        ResultActions response = mockMvc.perform(get("/api/v1/accounts/{accountId}", account.getId()));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName",
                        is(account.getFirstName())))
                .andExpect(jsonPath("$.surname",
                        is(account.getSurname())))
                .andExpect(jsonPath("$.gender",
                        is(account.getGender())))
                .andExpect(jsonPath("$.birthday",
                        is(df.format(account.getBirthday()))))
                .andExpect(jsonPath("$.zipCode",
                        is(account.getZipCode())));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void givenInvalidAccountId_whenGetAccountById_thenReturnEmpty() throws Exception {

        // given
        Long accountId = 19L;
        Account account = Account.builder()
                .firstName("Salim")
                .surname("Dursun")
                .gender("M")
                .birthday(new Date(1985-2-24))
                .zipCode("1000AA")
                .build();
        accountRepository.save(account);

        // when
        ResultActions response = mockMvc.perform(get("/api/v1/accounts/{accountId}", accountId).with(user("User")));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

//    @Test
//    @WithMockUser(roles = "ADMIN")
//    public void givenUpdatedAccount_whenUpdateAccount_thenReturnUpdateAccountObject() throws Exception {
//
//        // given
//        Account savedAccount = Account.builder()
//                .firstName("Kursad")
//                .surname("Dursun")
//                .gender("M")
//                .birthday(new Date(1985-2-24))
//                .zipCode("1000AA")
//                .build();
//        accountRepository.save(savedAccount);
//
//        Account updatedAccount = Account.builder()
//                .id(28L)
//                .firstName("Salim")
//                .surname("Dursun")
//                .gender("M")
//                .birthday(new Date(1985-2-24))
//                .zipCode("1060AA")
//                .build();
//
//        // when
//        ResultActions response = mockMvc.perform(put("/api/v1/accounts/{accountId}", savedAccount.getId()).with(user("User"))
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(updatedAccount)));
//        // then
//        response.andExpect(status().isOk())
//                .andDo(print())
//                .andExpect(jsonPath("$.firstName",
//                        is(updatedAccount.getFirstName())))
//                .andExpect(jsonPath("$.surname",
//                        is(updatedAccount.getSurname())))
//                .andExpect(jsonPath("$.gender",
//                        is(updatedAccount.getGender())))
//                .andExpect(jsonPath("$.birthday",
//                        is(updatedAccount.getBirthday())))
//                .andExpect(jsonPath("$.zipCode",
//                        is(updatedAccount.getZipCode())));
    }

//    @Test
//    @WithMockUser(roles = "ADMIN")
//    public void givenUpdatedAccount_whenUpdateAccount_thenReturn404() throws Exception {
//
//        // given
//        Long accountId = 29L;
//        Account savedAccount = Account.builder()
//                .firstName("Joo")
//                .surname("Peterson")
//                .gender("M")
//                .birthday(new Date(1995-5-11))
//                .zipCode("1346AB")
//                .build();
//        accountRepository.save(savedAccount);
//
//        Account updatedAccount = Account.builder()
//                .firstName("Joop")
//                .surname("Pieterson")
//                .gender("M")
//                .birthday(new Date(1995-5-11))
//                .zipCode("1136AB")
//                .build();
//
//        // when
//        ResultActions response = mockMvc.perform(put("/api/v1/accounts/{accountId}", accountId).with(user("Test"))
//                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updatedAccount)));
//
//        // then
//        response.andExpect(status().isNotFound())
//                .andDo(print());
//    }

//    @Test
//    @WithMockUser(roles = "ADMIN")
//    public void givenAccountId_whenDeleteAccount_thenReturn200() throws Exception {
//
//        // given
//        Account savedAccount = Account.builder()
//                .firstName("Salim")
//                .surname("Dursun")
//                .gender("M")
//                .birthday(new Date(1985-2-24))
//                .zipCode("1060AA")
//                .build();
//        accountRepository.save(savedAccount);
//
//        // when
//        ResultActions response = mockMvc.perform(delete("/api/v1/accounts/{accountId}", savedAccount.getId()).with(user("User")));
//
//        // then
//        response.andExpect(status().isOk())
//                .andDo(print());
//    }


