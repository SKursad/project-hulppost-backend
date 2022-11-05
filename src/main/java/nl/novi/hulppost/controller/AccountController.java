package nl.novi.hulppost.controller;

import nl.novi.hulppost.dto.AccountDTO;
import nl.novi.hulppost.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping({"/api/v1/accounts"})
public class AccountController {

    @Autowired
    private AccountService accountService;


    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        return new ResponseEntity<>(accountService.getAllAccounts(), HttpStatus.OK);
    }

    @GetMapping({"/{accountId}"})
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable("accountId") @Valid Long accountId) {
        return accountService.getAccountById(accountId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping("/{accountId}")
    @PreAuthorize("@methodLevelSecurityService.isAuthorizedAccount(#accountId, principal)")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable(value = "accountId") Long accountId,
                                                    @Valid @RequestBody AccountDTO accountDto) {
        return accountService.getAccountById(accountId)
                .map(savedAccount -> {
                    AccountDTO updatedAccount = accountService.updateAccount(accountDto, accountId);
                    return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping({"/{accountId}"})
    @PreAuthorize("@methodLevelSecurityService.isAuthorizedAccount(#accountId, principal)")
    public ResponseEntity<String> deleteAccount(@PathVariable("accountId") Long accountId) {
        this.accountService.deleteAccount(accountId);
        return new ResponseEntity<>("Profiel succesvol verwijderd ", HttpStatus.OK);
    }

}
