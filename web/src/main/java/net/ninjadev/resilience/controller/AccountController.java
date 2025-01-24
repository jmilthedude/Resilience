package net.ninjadev.resilience.controller;

import lombok.extern.slf4j.Slf4j;
import net.ninjadev.resilience.entity.Account;
import net.ninjadev.resilience.entity.ResilienceUser;
import net.ninjadev.resilience.service.AccountService;
import net.ninjadev.resilience.service.ResilienceUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    private final AccountService accountService;
    private final ResilienceUserService userService;

    @Autowired
    public AccountController(AccountService accountService, ResilienceUserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody Account account) {
        try {
            this.accountService.add(account);
            return ResponseEntity.ok("Account Added: id=" + account.getId());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Account Addition Failed: error=" + ex.getMessage());
        }
    }

    @PostMapping("/addUserToAccount")
    public ResponseEntity<String> add(@RequestParam String accountId, @RequestParam String userId) {
        try {
            Optional<ResilienceUser> user = this.userService.findById(userId);
            Optional<Account> account = this.accountService.findById(accountId);
            this.accountService.addUserToAccount(user.get(), account.get());
            return ResponseEntity.ok("User added to account: user=%s, accountId=%s".formatted(user.get().getUsername(), account.get().getId()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Account Addition Failed: error=" + ex.getMessage());
        }
    }

    @GetMapping("getByUserId")
    public ResponseEntity<List<Account>> getByUserId(@RequestParam Long userId) {
        return ResponseEntity.ok(this.accountService.getByUserId(userId));
    }
}
