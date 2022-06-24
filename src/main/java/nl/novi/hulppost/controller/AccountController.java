package nl.novi.hulppost.controller;

import nl.novi.hulppost.dto.AccountDto;
import nl.novi.hulppost.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping({"/hulppost/accounts"})
public class AccountController {

    @Autowired
    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        return new ResponseEntity<>(accountService.getAllAccounts(), HttpStatus.OK);
    }

    @GetMapping({"/{accountId}"})
    public ResponseEntity<AccountDto> getAccountById(@PathVariable("accountId") @Valid Long accountId) {
        return accountService.getAccountById(accountId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AccountDto> saveAccount(@RequestBody @Valid AccountDto accountDto) {
        return new ResponseEntity<>(accountService.saveAccount(accountDto), HttpStatus.CREATED);
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable(value = "accountId") Long accountId,
                                              @Valid @RequestBody AccountDto accountDto) {
        return accountService.getAccountById(accountId)
                .map(savedAccount -> {
                    AccountDto updatedAccount = accountService.updateAccount(accountDto, accountId);
                    return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping({"/{accountId}"})
    public ResponseEntity<String> deleteAccount(@PathVariable("accountId") Long accountId) {
        this.accountService.deleteAccount(accountId);
        return new ResponseEntity<>("Profiel succesvol verwijderd ", HttpStatus.OK);
    }

}
