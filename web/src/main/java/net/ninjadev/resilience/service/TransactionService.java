package net.ninjadev.resilience.service;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ninjadev.resilience.entity.transaction.Transaction;
import net.ninjadev.resilience.repository.transaction.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public List<Transaction> getAllTransactions() {
        return this.transactionRepository.findAll();
    }

    public Optional<Transaction> getTransactionById(Long id) {
        return this.transactionRepository.findById(id);
    }

    public Optional<Transaction> createTransaction(@Valid Transaction transaction) {
        return Optional.of(this.transactionRepository.save(transaction));
    }

    public Optional<Transaction> updateTransaction(Long id, @Valid Transaction transaction) {
        return this.transactionRepository.findById(id)
                .map(existingTransaction -> {
                    existingTransaction.setAmount(transaction.getAmount());
                    existingTransaction.setCategory(transaction.getCategory());
                    existingTransaction.setType(transaction.getType());
                    return this.transactionRepository.save(existingTransaction);
                });
    }

    public void deleteTransaction(Long id) {
        this.transactionRepository.deleteById(id);
    }
}
