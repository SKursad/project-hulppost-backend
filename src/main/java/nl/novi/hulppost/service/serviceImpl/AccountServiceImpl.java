package nl.novi.hulppost.service.serviceImpl;

import nl.novi.hulppost.dto.AccountDTO;
import nl.novi.hulppost.exception.ResourceNotFoundException;
import nl.novi.hulppost.model.Account;
import nl.novi.hulppost.repository.AccountRepository;
import nl.novi.hulppost.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Override
    public AccountDTO saveAccount(AccountDTO accountDTO) {
        Account account = mapToEntity(accountDTO);
        Optional<Account> savedAccount = accountRepository.findByFirstNameIgnoreCase(accountDTO.getFirstName());
        if (savedAccount.isPresent())
            throw new ResourceNotFoundException("Het profiel bestaat al");
        Account newAccount = accountRepository.save(account);
        return mapToDTO(newAccount);
    }


    @Override
    public List<AccountDTO> getAllAccounts() {
        List<Account> accountList = accountRepository.findAll();
        List<AccountDTO> accountDTOList = new ArrayList<>();


        for (Account account : accountList) {
            AccountDTO accountDTO = mapToDTO(account);
            accountDTOList.add(accountDTO);
        }

        return accountDTOList;
    }

    @Override
    public Optional<AccountDTO> getAccountById(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Profiel niet gevonden"));

        return Optional.of(mapToDTO(account));
    }

    @Override
    public AccountDTO updateAccount(AccountDTO accountDTO, Long accountId) {
        Account inDB = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Account", "id", accountId));

        inDB.setFirstName(accountDTO.getFirstName());
        inDB.setSurname(accountDTO.getSurname());
        inDB.setBirthday(accountDTO.getBirthday());
        inDB.setZipCode(accountDTO.getZipCode());
        inDB.setGender(accountDTO.getGender());
        Account updatedAccount = accountRepository.save(inDB);

        return mapToDTO(updatedAccount);
    }

    @Override
    public void deleteAccount(Long accountId) {
        accountRepository.deleteById(accountId);
    }

    private AccountDTO mapToDTO(Account account) {
        AccountDTO accountDTO = new AccountDTO();

        accountDTO.setId(account.getId());
        accountDTO.setFirstName(account.getFirstName());
        accountDTO.setSurname(account.getSurname());
        accountDTO.setBirthday(account.getBirthday());
        accountDTO.setZipCode(account.getZipCode());
        accountDTO.setGender(account.getGender());

        return accountDTO;
    }

    private Account mapToEntity(AccountDTO accountDTO) {
        Account account = new Account();

        account.setId(accountDTO.getId());
        account.setFirstName(accountDTO.getFirstName());
        account.setSurname(accountDTO.getSurname());
        account.setBirthday(accountDTO.getBirthday());
        account.setZipCode(accountDTO.getZipCode());
        account.setGender(accountDTO.getGender());

        return account;
    }

}

