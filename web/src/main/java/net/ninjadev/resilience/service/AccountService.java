package net.ninjadev.resilience.service;

import net.ninjadev.resilience.entity.Account;
import net.ninjadev.resilience.entity.ResilienceUser;
import net.ninjadev.resilience.repository.AccountRepository;
import net.ninjadev.resilience.repository.ResilienceUserRepository;
import net.ninjadev.resilience.request.AddAccountRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final ResilienceUserRepository userRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, ResilienceUserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public Optional<Account> findById(String id) {
        return this.accountRepository.findById(Long.parseLong(id));
    }

    public Optional<Account> add(AddAccountRequest account) {
        if (this.accountRepository.findByAccountNumber(account.getAccountNumber()).isPresent()) {
            return Optional.empty();
        }
        Account newAccount = new Account(account);
        return Optional.of(this.accountRepository.save(newAccount));
    }

    public void addUserToAccount(ResilienceUser user, Account account) {
        validateUser(user);
        validateAccount(account);

        Account found = accountRepository.findById(account.getId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + account.getId()));
        found.getUsers().add(user);
        this.accountRepository.save(account);
    }

    private void validateAccount(Account account) {
        if (account == null || account.getId() == null) {
            throw new IllegalArgumentException("Account and its ID must not be null");
        }
    }

    private void validateUser(ResilienceUser user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("User and its ID must not be null");
        }
        userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + user.getId()));

    }

    public List<Account> getByUserId(Long userId) {
        Optional<ResilienceUser> user = this.userRepository.findById(userId);
        if (user.isPresent()) {
            return this.getByUser(user.get());
        }
        return Collections.emptyList();
    }

    public List<Account> getByUser(ResilienceUser user) {
        return this.accountRepository.findByUsers(Set.of(user));
    }


    public List<Account> getAllAccounts() {
        return this.accountRepository.findAll();
    }
}
