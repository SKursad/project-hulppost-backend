package nl.novi.hulppost.service;

import nl.novi.hulppost.dto.AccountDto;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    AccountDto saveAccount(AccountDto accountDto);

    List<AccountDto> getAllAccounts();

    Optional<AccountDto> getAccountById(Long accountId);

    AccountDto updateAccount(AccountDto accountDto, Long accountId);

    void deleteAccount(Long accountId);
}
