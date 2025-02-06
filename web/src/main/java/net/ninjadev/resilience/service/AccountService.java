package net.ninjadev.resilience.service;

import lombok.RequiredArgsConstructor;
import net.ninjadev.resilience.entity.Account;
import net.ninjadev.resilience.entity.ResilienceUser;
import net.ninjadev.resilience.entity.transaction.RecurringTransaction;
import net.ninjadev.resilience.entity.transaction.Transaction;
import net.ninjadev.resilience.repository.AccountRepository;
import net.ninjadev.resilience.repository.ResilienceUserRepository;
import net.ninjadev.resilience.repository.transaction.RecurringTransactionRepository;
import net.ninjadev.resilience.repository.transaction.TransactionRepository;
import net.ninjadev.resilience.request.AddAccountRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final RecurringTransactionRepository recurringTransactionRepository;
    private final ResilienceUserRepository userRepository;

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

    public void update(Account account) {
        this.accountRepository.save(account);
    }

    public double getPendingBalance(long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found for ID: " + accountId));

        List<Transaction> pendingTransactions = transactionRepository.findPendingTransactionsByAccountId(accountId);

        double pendingTransactionSum = pendingTransactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();

        return account.getBalance() + pendingTransactionSum;
    }

    public double getBalanceAsOfDate(long accountId, LocalDate date) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found for ID: " + accountId));

        double pendingBalance = this.getPendingBalance(accountId);

        List<RecurringTransaction> recurringTransactions = recurringTransactionRepository.findAll();

        double recurringTransactionSum = recurringTransactions.stream()
                .flatMap(recurring -> {
                    List<LocalDate> occurrences = recurring.computeAllOccurrences(LocalDate.now(), date);
                    return occurrences.stream().map(occurrence -> recurring.getAmount());
                })
                .mapToDouble(amount -> amount)
                .sum();

        return account.getBalance() + pendingBalance + recurringTransactionSum;
    }


}
