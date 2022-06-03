package nl.novi.hulppost.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.hulppost.dto.AccountDto;
import nl.novi.hulppost.model.Account;
import nl.novi.hulppost.model.enums.Gender;
import nl.novi.hulppost.repository.AccountRepository;
import nl.novi.hulppost.service.AccountService;
import nl.novi.hulppost.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    public AccountRepository accountRepository;


    @Test
    public void givenAccountObject_whenCreateAccount_thenReturnSavedAccount() throws Exception {

        // given - precondition or setup
        Account account = Account.builder()
                .id(1L)
                .firstName("Test")
                .surname("Person")
                .birthday("22/02/02")
                .gender(Gender.M).
                telNumber("06123456789")
                .zipCode("1455AZ")
                .build();

        given(accountService.saveAccount(any(AccountDto.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when - action that's under test
        ResultActions response = mockMvc.perform(post("/hulppost/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(account)));

        // then - verify output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName",
                        is(account.getFirstName())))
                .andExpect(jsonPath("$.surname",
                        is(account.getSurname())))
                .andExpect(jsonPath("$.birthday",
                        is(account.getBirthday())))
                .andExpect(jsonPath("$.gender",
                        is(account.getGender().toString())))
                .andExpect(jsonPath("$.telNumber",
                        is(account.getTelNumber())))
                .andExpect(jsonPath("$.zipCode",
                        is(account.getZipCode())));
    }

    @Test
    public void givenListOfAccounts_whenGetAllAccounts_thenReturnAccountsList() throws Exception {

        // given
        List<AccountDto> listOfAccounts = new ArrayList();
        listOfAccounts.add(AccountDto.builder()
                .id(1L)
                .firstName("Kursad")
                .surname("Dursun")
                .birthday("24/02/1985")
                .gender(Gender.M)
                .telNumber("06123456789")
                .zipCode("1000AA")
                .build());

        listOfAccounts.add(AccountDto.builder()
                .id(2L)
                .firstName("Dummy")
                .surname("Tekst")
                .birthday("01/02/2000")
                .gender(Gender.M)
                .telNumber("06987654321")
                .zipCode("1000AA")
                .build());
        given(accountService.getAllAccounts()).willReturn(listOfAccounts);

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
        Long accountId = 1L;
        AccountDto accountDto = AccountDto.builder()
                .id(1L)
                .firstName("Test")
                .surname("Person")
                .birthday("22/07/2002")
                .gender(Gender.V)
                .telNumber("06123456789")
                .zipCode("1455AZ")
                .build();
        given(accountService.getAccountById(accountId)).willReturn(Optional.of(accountDto));

        // when
        ResultActions response = mockMvc.perform(get("/hulppost/accounts/{accountId}", accountId));

        // then
        response.andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.firstName",
                        is(accountDto.getFirstName())))
                .andExpect(jsonPath("$.surname",
                        is(accountDto.getSurname())))
                .andExpect(jsonPath("$.birthday",
                        is(accountDto.getBirthday())))
                .andExpect(jsonPath("$.gender",
                        is(accountDto.getGender().toString())))
                .andExpect(jsonPath("$.telNumber",
                        is(accountDto.getTelNumber())))
                .andExpect(jsonPath("$.zipCode",
                        is(accountDto.getZipCode())))
                .andExpect(jsonPath("$.gender",
                        is(Gender.V.toString())));
    }

    @Test
    public void givenInvalidAccountId_whenGetAccountById_thenReturnEmpty() throws Exception {

        // given
        Long accountId = 1L;
        AccountDto accountDto = AccountDto.builder()
                .id(1L)
                .firstName("Anna")
                .surname("Gouda")
                .birthday("22/07/02")
                .gender(Gender.V)
                .telNumber("0635790135")
                .zipCode("1445MM")
                .build();
        given(accountService.getAccountById(accountId)).willReturn(Optional.empty());

        // when
        ResultActions response = mockMvc.perform(get("/hulppost/accounts/{accountId}", accountId));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenUpdatedAccount_whenUpdateAccount_thenReturnUpdateAccountObject() throws Exception {

        // given
        Long accountId = 1L;
        AccountDto savedAccount = AccountDto.builder()
                .firstName("Test")
                .surname("Person")
                .birthday("18/04/2002")
                .gender(Gender.M)
                .telNumber("06123456789")
                .zipCode("1455AZ")
                .build();

        AccountDto updatedAccount = AccountDto.builder()
                .id(1L)
                .firstName("Joop")
                .surname("Peterson")
                .birthday("18/04/2002")
                .gender(Gender.M)
                .telNumber("06123456789")
                .zipCode("1455AZ")
                .build();
        given(accountService.getAccountById(accountId)).willReturn(Optional.of(savedAccount));
        given(accountService.updateAccount((AccountDto) any(AccountDto.class), anyLong()))
                .willAnswer((invocation) -> invocation.getArgument(0));


        // when
        ResultActions response = mockMvc.perform(put("/hulppost/accounts/{accountId}", accountId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedAccount)));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName",
                        is(updatedAccount.getFirstName())))
                .andExpect(jsonPath("$.surname",
                        is(updatedAccount.getSurname())))
                .andExpect(jsonPath("$.birthday",
                        is(updatedAccount.getBirthday())))
                .andExpect(jsonPath("$.gender",
                        is(updatedAccount.getGender().toString())))
                .andExpect(jsonPath("$.telNumber",
                        is(updatedAccount.getTelNumber())))
                .andExpect(jsonPath("$.zipCode",
                        is(updatedAccount.getZipCode())));
    }


    @Test
    public void givenUpdatedAccount_whenUpdateAccount_thenReturn404() throws Exception {

        // given
        Long accountId = 1L;
        AccountDto savedAccount = AccountDto.builder()
                .userId(1L)
                .firstName("Test")
                .surname("Person")
                .birthday("22/07/2002")
                .gender(Gender.V)
                .telNumber("06123456789")
                .zipCode("1455AZ")
                .build();

        AccountDto updatedAccount = AccountDto.builder()
                .id(1L)
                .userId(1L)
                .firstName("Test")
                .surname("Person")
                .birthday("22/07/2002")
                .gender(Gender.V)
                .telNumber("06123456789")
                .zipCode("1455AZ")
                .build();
        given(accountService.getAccountById(accountId)).willReturn(Optional.empty());
        given(accountService.updateAccount((AccountDto) any(AccountDto.class), anyLong()))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when
        ResultActions response = mockMvc.perform(put("/hulppost/accounts/{accountId}", accountId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedAccount)));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }


    @Test
    public void givenAccountId_whenDeleteAccount_thenReturn200() throws Exception {

        // given
        Long accountId = 1L;
        willDoNothing().given(accountService).deleteAccount(accountId);

        // when
        ResultActions response = mockMvc.perform(delete("/hulppost/accounts/{accountId}", accountId));

        // then
        response.andExpect(status().isOk())
                .andDo(print());
    }

}

