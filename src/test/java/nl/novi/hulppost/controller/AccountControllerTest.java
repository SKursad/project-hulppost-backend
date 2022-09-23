package nl.novi.hulppost.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.hulppost.config.AppConfiguration;
import nl.novi.hulppost.config.WebConfiguration;
import nl.novi.hulppost.dto.AccountDTO;
import nl.novi.hulppost.dto.RegistrationDTO;
import nl.novi.hulppost.model.Account;
import nl.novi.hulppost.repository.AccountRepository;
import nl.novi.hulppost.repository.UserRepository;
import nl.novi.hulppost.security.CustomUserDetailsService;
import nl.novi.hulppost.security.JwtAuthenticationEntryPoint;
import nl.novi.hulppost.security.JwtAuthenticationFilter;
import nl.novi.hulppost.security.JwtTokenProvider;
import nl.novi.hulppost.service.*;
import nl.novi.hulppost.service.serviceImpl.FileServiceImpl;
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
@AutoConfigureMockMvc(addFilters = false)
public class AccountControllerTest {

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
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Mock
    private PasswordEncoder passwordEncoder;

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
    public void givenListOfAccounts_whenGetAllAccounts_thenReturnAccountsList() throws Exception {

        // given
        List<AccountDTO> listOfAccounts = new ArrayList<>();
        listOfAccounts.add(AccountDTO.builder()
                .id(1L)
                .firstName("Kursad")
                .surname("Dursun")
                .birthday("24/02/1985")
                .gender("M")
                .zipCode("1000AA")
                .build());

        listOfAccounts.add(AccountDTO.builder()
                .id(2L)
                .firstName("Dummy")
                .surname("Tekst")
                .birthday("01/02/2000")
                .gender("M")
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
        AccountDTO accountDto = AccountDTO.builder()
                .id(1L)
                .firstName("Test")
                .surname("Person")
                .birthday("22/07/2002")
                .gender("V")
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
                        is(accountDto.getGender())))
                .andExpect(jsonPath("$.zipCode",
                        is(accountDto.getZipCode())));
    }

    @Test
    public void givenInvalidAccountId_whenGetAccountById_thenReturnEmpty() throws Exception {

        // given
        Long accountId = 1L;
        AccountDTO accountDto = AccountDTO.builder()
                .id(1L)
                .firstName("Anna")
                .surname("Gouda")
                .birthday("22/07/2002")
                .gender("V")
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
        AccountDTO savedAccount = AccountDTO.builder()
                .firstName("Test")
                .surname("Person")
                .birthday("18/04/1997")
                .gender("M")
                .zipCode("1455AZ")
                .build();

        AccountDTO updatedAccount = AccountDTO.builder()
                .id(1L)
                .firstName("Joop")
                .surname("Peterson")
                .birthday("18/04/1984")
                .gender("M")
                .zipCode("1455AZ")
                .build();
        given(accountService.getAccountById(accountId)).willReturn(Optional.of(savedAccount));
        given(accountService.updateAccount((AccountDTO) any(AccountDTO.class), anyLong()))
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
                        is(updatedAccount.getGender())))
                .andExpect(jsonPath("$.zipCode",
                        is(updatedAccount.getZipCode())));
    }


    @Test
    public void givenUpdatedAccount_whenUpdateAccount_thenReturn404() throws Exception {

        // given
        Long id = 1L;
        AccountDTO savedAccount = AccountDTO.builder()
                .firstName("Test")
                .surname("Person")
                .birthday("22/07/1999")
                .gender("V")
                .zipCode("1455AZ")
                .build();

        AccountDTO updatedAccount = AccountDTO.builder()
                .id(1L)
                .firstName("Test")
                .surname("Person")
                .birthday("22/07/1999")
                .gender("V")
                .zipCode("1455AZ")
                .build();
        given(accountService.getAccountById(id)).willReturn(Optional.empty());
        given(accountService.updateAccount((AccountDTO) any(AccountDTO.class), anyLong()))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when
        ResultActions response = mockMvc.perform(put("/hulppost/accounts/{accountId}", id)
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

