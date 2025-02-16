package net.ninjadev.resilience.controller;

import lombok.extern.slf4j.Slf4j;
import net.ninjadev.resilience.entity.Account;
import net.ninjadev.resilience.entity.ResilienceUser;
import net.ninjadev.resilience.request.AddAccountRequest;
import net.ninjadev.resilience.response.AccountResponse;
import net.ninjadev.resilience.response.ResilienceUserResponse;
import net.ninjadev.resilience.service.AccountService;
import net.ninjadev.resilience.service.ResilienceUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;
    private final ResilienceUserService userService;

    @Autowired
    public AccountController(AccountService accountService, ResilienceUserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        return ResponseEntity.ok(this.accountService.getAllAccounts().stream().map(AccountResponse::new).toList());
    }

    @GetMapping("{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable String id) {
        return this.accountService.findById(id).map(AccountResponse::new).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AccountResponse> addAccount(@RequestBody AddAccountRequest account) {
        return this.accountService.add(account)
                .map(addedAccount -> ResponseEntity.ok(new AccountResponse(addedAccount)))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{accountId}/addUser/{userId}")
    public ResponseEntity<String> addUserToAccount(@PathVariable String accountId, @PathVariable String userId) {
        try {
            ResilienceUser user = this.userService.findById(userId).orElseThrow();
            Account account = this.accountService.findById(accountId).orElseThrow();
            this.accountService.addUserToAccount(user, account);
            return ResponseEntity.ok("User added to account: user=%s, accountId=%s".formatted(user.getUsername(), account.getId()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Account Addition Failed: error=" + ex.getMessage());
        }
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<AccountResponse>> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(this.accountService.getByUserId(userId).stream().map(AccountResponse::new).toList());
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteAccount(@PathVariable String id) {
        if (this.accountService.deleteById(id)) {
            return ResponseEntity.ok("Account deleted successfully.");
        }
        return ResponseEntity.notFound().build();
    }
}
