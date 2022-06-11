package nl.novi.hulppost.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.hulppost.model.Account;
import nl.novi.hulppost.model.enums.Gender;
import nl.novi.hulppost.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
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
    private ObjectMapper objectMapper;


    @Test
    public void givenAccountObject_whenCreateAccount_thenReturnSavedAccount() throws Exception {

        // given - precondition or setup
        Account account = Account.builder()
                .id(1L)
                .firstName("DummyPerson")
                .surname("Tester")
                .gender(Gender.M)
                .zipCode("1000AA")
                .birthday("22/04/1999")
                .build();

        // when - action that's under test
        ResultActions response = mockMvc.perform(post("/hulppost/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(account)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName",
                        is(account.getFirstName())))
                .andExpect(jsonPath("$.surname",
                        is(account.getSurname())))
                .andExpect(jsonPath("$.gender",
                        is(Gender.M.toString())))
                .andExpect(jsonPath("$.zipCode",
                        is(account.getZipCode())))
                .andExpect(jsonPath("$.birthday",
                        is(account.getBirthday())));
    }

    @Test
    public void givenListOfAccounts_whenGetAllAccounts_thenReturnAccountsList() throws Exception {

        // given
        List<Account> listOfAccounts = new ArrayList();
        listOfAccounts.add(Account.builder()
                .firstName("Kursad")
                .surname("Dursun")
                .zipCode("1234AA")
                .build());

        listOfAccounts.add(Account.builder()
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
    public void givenAccountId_whenGetAccountById_thenReturnAccountObject() throws Exception {

        // given
        Account account = Account.builder()
                .firstName("Kursad")
                .surname("Dursun")
                .gender(Gender.M)
                .birthday("24/02/1985")
                .zipCode("1000AA")
                .telNumber("061234567890")
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
                        is(Gender.M.toString())))
                .andExpect(jsonPath("$.birthday",
                        is(account.getBirthday())))
                .andExpect(jsonPath("$.zipCode",
                        is(account.getZipCode())))
                .andExpect(jsonPath("$.telNumber",
                        is(account.getTelNumber())));
    }

    @Test
    public void givenInvalidAccountId_whenGetAccountById_thenReturnEmpty() throws Exception {

        // given
        Long accountId = 3L;
        Account account = Account.builder()
                .firstName("Salim")
                .surname("Dursun")
                .gender(Gender.M)
                .birthday("24/02/1985")
                .zipCode("1000AA")
                .telNumber("061234567890")
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
                .gender(Gender.M)
                .birthday("24/2/1985")
                .zipCode("1000AA")
                .telNumber("061234567890")
                .build();
        accountRepository.save(savedAccount);

        Account updatedAccount = Account.builder()
                .id(1L)
                .firstName("Salim")
                .surname("Dursun")
                .gender(Gender.M)
                .birthday("24/2/1985")
                .zipCode("1060AA")
                .telNumber("060987654321")
                .build();

        // when
        ResultActions response = mockMvc.perform(put("/hulppost/accounts/{accountId}", savedAccount.getId())
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
                        is(updatedAccount.getGender().toString())))
                .andExpect(jsonPath("$.birthday",
                        is(updatedAccount.getBirthday())))
                .andExpect(jsonPath("$.zipCode",
                        is(updatedAccount.getZipCode())))
                .andExpect(jsonPath("$.telNumber",
                        is(updatedAccount.getTelNumber())));
    }

    @Test
    public void givenUpdatedAccount_whenUpdateAccount_thenReturn404() throws Exception {

        // given
        Long accountId = 2L;
        Account savedAccount = Account.builder()
                .firstName("Joo")
                .surname("Peterson")
                .gender(Gender.M)
                .birthday("11/05/1995")
                .zipCode("1346AB")
                .telNumber("060987654321")
                .build();
        accountRepository.save(savedAccount);

        Account updatedAccount = Account.builder()
                .id(8L)
                .firstName("Joop")
                .surname("Pieterson")
                .gender(Gender.M)
                .birthday("11/05/1995")
                .zipCode("1136AB")
                .telNumber("060987654321")
                .build();

        // when
        ResultActions response = mockMvc.perform(put("/hulppost/accounts/{accountId}", accountId)
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
                .gender(Gender.M)
                .birthday("24/02/1985")
                .zipCode("1060AA")
                .telNumber("060987654321")
                .build();
        accountRepository.save(savedAccount);

        // when
        ResultActions response = mockMvc.perform(delete("/hulppost/accounts/{accountId}", savedAccount.getId()));

        // then
        response.andExpect(status().isOk())
                .andDo(print());
    }
}

