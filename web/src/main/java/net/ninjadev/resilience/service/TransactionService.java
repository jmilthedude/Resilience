package net.ninjadev.resilience.service;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ninjadev.resilience.entity.transaction.Transaction;
import net.ninjadev.resilience.event.TransactionPostedEvent;
import net.ninjadev.resilience.repository.transaction.TransactionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final ApplicationEventPublisher eventPublisher;

    public List<Transaction> getAllTransactions() {
        return this.transactionRepository.findAll();
    }

    public Optional<Transaction> getTransactionById(Long id) {
        return this.transactionRepository.findById(id);
    }

    public Optional<Transaction> createTransaction(@Valid Transaction transaction) {
        Transaction savedTransaction = this.transactionRepository.save(transaction);
        if (transaction.isPosted()) {
            this.eventPublisher.publishEvent(savedTransaction);
        }
        return Optional.of(savedTransaction);
    }

    public boolean markTransactionAsPosted(long id) {
        Optional<Transaction> transaction = this.transactionRepository.findById(id);
        if (transaction.isEmpty()) {
            return false;
        }
        Transaction existingTransaction = transaction.get();
        existingTransaction.setPosted(true);
        this.transactionRepository.save(existingTransaction);
        this.eventPublisher.publishEvent(new TransactionPostedEvent(existingTransaction));
        return true;
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
