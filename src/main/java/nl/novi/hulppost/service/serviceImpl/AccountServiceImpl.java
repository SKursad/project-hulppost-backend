package nl.novi.hulppost.service.serviceImpl;

import nl.novi.hulppost.dto.AccountDTO;
import nl.novi.hulppost.dto.RegistrationDTO;
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
    public AccountDTO saveAccount(AccountDTO accountDto) {
        Account account = mapToEntity(accountDto);
        Optional<Account> savedAccount = accountRepository.findByFirstNameIgnoreCase(accountDto.getFirstName());
        if (savedAccount.isPresent())
            throw new ResourceNotFoundException("Het profiel bestaat al");
        Account newAccount = accountRepository.save(account);
        return mapToDto(newAccount);
    }


    @Override
    public List<AccountDTO> getAllAccounts() {
        List<Account> accountList = accountRepository.findAll();
        List<AccountDTO> accountDTOList = new ArrayList();


        for (Account account : accountList) {
            AccountDTO accountDto = mapToDto(account);
            accountDTOList.add(accountDto);
        }

        return accountDTOList;
    }

    @Override
    public Optional<AccountDTO> getAccountById(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Profiel niet gevonden"));

        return Optional.of(mapToDto(account));
    }

    @Override
    public AccountDTO updateAccount(AccountDTO accountDto, Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Account", "id", accountId));

        account.setFirstName(accountDto.getFirstName());
        account.setSurname(accountDto.getSurname());
        account.setBirthday(accountDto.getBirthday());
        account.setZipCode(accountDto.getZipCode());
        account.setGender(accountDto.getGender());
        Account updatedAccount = accountRepository.save(account);

        return mapToDto(updatedAccount);
    }

    @Override
    public void deleteAccount(Long accountId) {
        accountRepository.deleteById(accountId);
    }

    private AccountDTO mapToDto(Account account) {
        AccountDTO accountDto = new AccountDTO();

        accountDto.setId(account.getId());
        accountDto.setFirstName(account.getFirstName());
        accountDto.setSurname(account.getSurname());
        accountDto.setBirthday(account.getBirthday());
        accountDto.setZipCode(account.getZipCode());
        accountDto.setGender(account.getGender());

        return accountDto;
    }

    private Account mapToEntity(AccountDTO accountDto) {
        Account account = new Account();

        account.setId(accountDto.getId());
        account.setFirstName(accountDto.getFirstName());
        account.setSurname(accountDto.getSurname());
        account.setBirthday(accountDto.getBirthday());
        account.setZipCode(accountDto.getZipCode());
        account.setGender(accountDto.getGender());;

        return account;
    }

}

