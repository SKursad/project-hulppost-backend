package nl.novi.hulppost.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.hulppost.config.AppConfiguration;
import nl.novi.hulppost.config.WebConfiguration;
import nl.novi.hulppost.dto.RegistrationDTO;
import nl.novi.hulppost.model.Account;
import nl.novi.hulppost.model.User;
import nl.novi.hulppost.repository.AccountRepository;
import nl.novi.hulppost.repository.RoleRepository;
import nl.novi.hulppost.repository.UserRepository;
import nl.novi.hulppost.service.serviceImpl.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    AppConfiguration appConfiguration;

    @MockBean
    FileServiceImpl fileService;

    @MockBean
    WebConfiguration webConfiguration;

    @BeforeEach
    void setup(){
        accountRepository.deleteAll();
    }

    @Test
//    @WithMockUser (roles = "ADMIN")
    public void givenAccountObject_whenCreateAccount_thenReturnSavedAccount() throws Exception {

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
                .birthday("22/04/1999")
                .build();


        // when - action that's under test
        ResultActions response = mockMvc.perform(post("/auth/registration/helpSeeker").with(user("Test"))
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
                        is(helpSeeker.getBirthday())));
    }

    @Test
    @WithMockUser (roles = "ADMIN")
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
        ResultActions response = mockMvc.perform(get("/hulppost/accounts"));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfAccounts.size())));
    }

    @Test
    @WithMockUser (roles = "ADMIN")
    public void givenAccountId_whenGetAccountById_thenReturnAccountObject() throws Exception {

        // given
        Account account = Account.builder()
                .firstName("Kursad")
                .surname("Dursun")
                .gender("M")
                .birthday("24/02/1985")
                .zipCode("1000AA")
                .build();
        accountRepository.save(account);

        // when
        ResultActions response = mockMvc.perform(get("/hulppost/accounts/{accountId}", account.getId()));

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
                        is(account.getBirthday())))
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
                .birthday("24/02/1985")
                .zipCode("1000AA")
                .build();
        accountRepository.save(account);

        // when
        ResultActions response = mockMvc.perform(get("/hulppost/accounts/{accountId}", accountId));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenUpdatedAccount_whenUpdateAccount_thenReturnUpdateAccountObject() throws Exception {

        // given
        Account savedAccount = Account.builder()
                .firstName("Kursad")
                .surname("Dursun")
                .gender("M")
                .birthday("24/02/1985")
                .zipCode("1000AA")
                .build();
        accountRepository.save(savedAccount);

        Account updatedAccount = Account.builder()
                .id(28L)
                .firstName("Salim")
                .surname("Dursun")
                .gender("M")
                .birthday("24/02/1985")
                .zipCode("1060AA")
                .build();

        // when
        ResultActions response = mockMvc.perform(put("/hulppost/accounts/{accountId}", savedAccount.getId()).with(user("Test"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedAccount)));
        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName",
                        is(updatedAccount.getFirstName())))
                .andExpect(jsonPath("$.surname",
                        is(updatedAccount.getSurname())))
                .andExpect(jsonPath("$.gender",
                        is(updatedAccount.getGender())))
                .andExpect(jsonPath("$.birthday",
                        is(updatedAccount.getBirthday())))
                .andExpect(jsonPath("$.zipCode",
                        is(updatedAccount.getZipCode())));
    }

    @Test
    public void givenUpdatedAccount_whenUpdateAccount_thenReturn404() throws Exception {

        // given
        Long accountId = 29L;
        Account savedAccount = Account.builder()
                .firstName("Joo")
                .surname("Peterson")
                .gender("M")
                .birthday("11/05/1995")
                .zipCode("1346AB")
                .build();
        accountRepository.save(savedAccount);

        Account updatedAccount = Account.builder()
                .firstName("Joop")
                .surname("Pieterson")
                .gender("M")
                .birthday("11/05/1995")
                .zipCode("1136AB")
                .build();

        // when
        ResultActions response = mockMvc.perform(put("/hulppost/accounts/{accountId}", accountId).with(user("Test"))
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updatedAccount)));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenAccountId_whenDeleteAccount_thenReturn200() throws Exception {

        // given
        Account savedAccount = Account.builder()
                .firstName("Salim")
                .surname("Dursun")
                .gender("M")
                .birthday("24/02/1985")
                .zipCode("1060AA")
                .build();
        accountRepository.save(savedAccount);

        // when
        ResultActions response = mockMvc.perform(delete("/hulppost/accounts/{accountId}", savedAccount.getId()).with(user("Test")));

        // then
        response.andExpect(status().isOk())
                .andDo(print());
    }
}

