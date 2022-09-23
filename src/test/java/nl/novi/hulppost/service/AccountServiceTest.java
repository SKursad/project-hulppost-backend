package nl.novi.hulppost.service;

import nl.novi.hulppost.dto.AccountDTO;
import nl.novi.hulppost.exception.ResourceNotFoundException;
import nl.novi.hulppost.model.Account;
import nl.novi.hulppost.repository.AccountRepository;
import nl.novi.hulppost.service.serviceImpl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountServiceImpl underTest;
    private AccountDTO accountDto;
    private Account account;

    @BeforeEach
    public void setup() {
//        accountRepository = mock(AccountRepository.class);
//        underTest = new AccountServiceImpl(accountRepository);

        account = Account.builder()
                .id(1L)
                .firstName("DummyPerson")
                .surname("Tester")
                .gender("M")
                .zipCode("1000AA")
                .birthday("22/04/1999")
                .build();

        accountDto = AccountDTO.builder()
                .id(1L)
                .firstName("DummyPerson")
                .surname("Tester")
                .gender("M")
                .zipCode("1000AA")
                .birthday("22/04/1999")
                .build();
    }

    @DisplayName("JUnit test for saveAccount method")
    @Test
    public void givenAccountObject_whenSaveAccount_thenReturnAccountObject() {

        // given - precondition or setup
        lenient().when(accountRepository.findByFirstNameIgnoreCase(account.getFirstName()))
                .thenReturn(Optional.empty());

        given(accountRepository.save(account)).willReturn(account);

        System.out.println(accountRepository);
        System.out.println(underTest);

        // when - action that's under test
        Account newAccount = accountRepository.save(account);

        System.out.println(newAccount);

        // then - verify output
        assertThat(newAccount).isNotNull();

    }

    @DisplayName("JUnit test for saveAccount method which throws exception")
    @Test
    public void givenExistingEmail_whenSaveAccount_thenThrowsException() {

        // given
        when(accountRepository.findByFirstNameIgnoreCase(account.getFirstName()))
                .thenReturn(Optional.of(account));


        System.out.println(accountRepository);
        System.out.println(underTest);

        // when
        assertThrows(ResourceNotFoundException.class, () -> {
            underTest.saveAccount(accountDto);
        });

        // then
        verify(accountRepository, never()).save((Account) any(Account.class));
    }

    @DisplayName("JUnit test for getAllAccounts method")
    @Test
    public void givenAccountsList_whenGetAllAccounts_thenReturnAccountsList() {

        // given
        Account account1 = Account.builder()
                .id(1L)
                .firstName("DummyPerson")
                .surname("Tester")
                .gender("M")
                .zipCode("1000AA")
                .birthday("22/04/1999")
                .build();
        given(accountRepository.findAll()).willReturn(List.of(account, account1));


        // when
        List<AccountDTO> accountList = underTest.getAllAccounts();

        // then
        assertThat(accountList).isNotNull();
        assertThat(accountList.size()).isEqualTo(2);

    }

    @DisplayName("JUnit test for getAllAccounts method (negative scenario)")
    @Test
    public void givenEmptyAccountsList_whenGetAllAccounts_thenReturnEmptyAccountsList() {

        // given
        Account account1 = Account.builder()
                .id(2L)
                .firstName("DummyPerson")
                .surname("Tester")
                .gender("M")
                .zipCode("1000AA")
                .birthday("22/04/1999")
                .build();
        given(accountRepository.findAll()).willReturn(Collections.emptyList());

        // when
        List<AccountDTO> accountList = underTest.getAllAccounts();

        // then
        assertThat(accountList).isEmpty();
        assertThat(accountList.size()).isEqualTo(0);

    }

    @DisplayName("JUnit test for getAccountById method")
    @Test
    public void givenAccountId_whenGetAccountById_thenReturnAccountObject() {

        // given
        given(accountRepository.findById(1L))
                .willReturn(Optional.of(account));

        // when
        AccountDTO savedAccount = underTest.getAccountById(account.getId()).get();

        // then
        assertThat(savedAccount).isNotNull();

    }

    @DisplayName("JUnit test for updateAccount method")
    @Test
    public void givenAccountObject_whenUpdateAccount_thenReturnUpdatedAccount() {

        //Setup
        account.setId(1L);
        account.setFirstName("Salim Kurshad");
        account.setSurname("Dursun");
        account.setBirthday("24-02-85");

        accountDto.setId(1L);
        accountDto.setFirstName("Kursad");
        accountDto.setSurname("Dursun");
        accountDto.setBirthday("24-02-85");

        // given
        given(accountRepository.save(account)).willReturn(account);
        given(accountRepository.findById(1L)).willReturn(Optional.of(account));

        // when
        AccountDTO updatedAccount = underTest.updateAccount(accountDto, account.getId());

        // then
        assertThat(updatedAccount.getId()).isEqualTo(1L);
        assertThat(updatedAccount.getFirstName()).isEqualTo("Kursad");
        assertThat(updatedAccount.getSurname()).isEqualTo("Dursun");
        assertThat(updatedAccount.getBirthday()).isEqualTo("24-02-85");

    }

    @DisplayName("JUnit test for deleteAccount method")
    @Test
    public void givenAccountId_whenDeleteAccount_thenNothing() {

        // given
        Long accountId = 1L;
        willDoNothing().given(accountRepository).deleteById(accountId);

        // when
        underTest.deleteAccount(accountId);

        // then
        verify(accountRepository, times(1)).deleteById(accountId);

    }

}

