package nl.novi.hulppost.service;

import nl.novi.hulppost.dto.AccountDTO;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    AccountDTO saveAccount(AccountDTO accountDto);

    List<AccountDTO> getAllAccounts();

    Optional<AccountDTO> getAccountById(Long accountId);

    AccountDTO updateAccount(AccountDTO accountDto, Long accountId);

    void deleteAccount(Long accountId);

}
