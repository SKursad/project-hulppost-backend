package nl.novi.hulppost.service.serviceImpl;

import nl.novi.hulppost.dto.AccountDto;
import nl.novi.hulppost.exception.ResourceNotFoundException;
import nl.novi.hulppost.model.Account;
import nl.novi.hulppost.repository.AccountRepository;
import nl.novi.hulppost.service.AccountService;
import org.modelmapper.ModelMapper;
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
    public AccountDto saveAccount(AccountDto accountDto) {
        Account account = mapToEntity(accountDto);
        Optional<Account> savedAccount = accountRepository.findByFirstNameIgnoreCase(accountDto.getFirstName());
        if (savedAccount.isPresent())
            throw new ResourceNotFoundException("Het profiel bestaat al");
        Account newAccount = accountRepository.save(account);
        return mapToDto(newAccount);
    }


    @Override
    public List<AccountDto> getAllAccounts() {
        List<Account> accountList = accountRepository.findAll();
        List<AccountDto> accountDtoList = new ArrayList();


        for (Account account : accountList) {
            AccountDto accountDto = mapToDto(account);
            accountDtoList.add(accountDto);
        }

        return accountDtoList;
    }

    @Override
    public Optional<AccountDto> getAccountById(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Profiel niet gevonden"));

        return Optional.of(mapToDto(account));
    }

    @Override
    public AccountDto updateAccount(AccountDto accountDto, Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Account", "id", accountId));

        account.setFirstName(accountDto.getFirstName());
        account.setSurname(accountDto.getSurname());
        account.setBirthday(account.getBirthday());
        account.setZipCode(accountDto.getZipCode());
        account.setTelNumber(accountDto.getTelNumber());
        account.setGender(accountDto.getGender());
        Account updatedAccount = accountRepository.save(account);

        return mapToDto(updatedAccount);
    }

    @Override
    public void deleteAccount(Long accountId) {
        accountRepository.deleteById(accountId);
    }

    private AccountDto mapToDto(Account account) {
        AccountDto accountDto = new AccountDto();

        accountDto.setId(account.getId());
        accountDto.setUserId(account.getId());
        accountDto.setFirstName(account.getFirstName());
        accountDto.setSurname(account.getSurname());
        accountDto.setBirthday(account.getBirthday());
        accountDto.setZipCode(account.getZipCode());
        accountDto.setTelNumber(account.getTelNumber());
        accountDto.setGender(account.getGender());

        return accountDto;
//        return mapper.map(account, AccountDto.class);
    }

    private Account mapToEntity(AccountDto accountDto) {
        Account account = new Account();

        account.setId(accountDto.getId());
        account.setId(accountDto.getUserId());
        account.setFirstName(accountDto.getFirstName());
        account.setSurname(accountDto.getSurname());
        account.setBirthday(accountDto.getBirthday());
        account.setZipCode(accountDto.getZipCode());
        account.setTelNumber(accountDto.getTelNumber());
        account.setGender(accountDto.getGender());;

        return account;

//        return mapper.map(accountDto, Account.class);
    }

}

