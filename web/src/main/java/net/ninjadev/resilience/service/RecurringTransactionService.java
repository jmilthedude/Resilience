package net.ninjadev.resilience.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ninjadev.resilience.entity.transaction.RecurringTransaction;
import net.ninjadev.resilience.entity.transaction.Transaction;
import net.ninjadev.resilience.repository.transaction.RecurringTransactionRepository;
import net.ninjadev.resilience.repository.transaction.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecurringTransactionService {

    private final TransactionRepository transactionRepository;
    private final RecurringTransactionRepository recurringTransactionRepository;

    @Transactional
    public void process() {

        List<RecurringTransaction> recurringTransactions = this.recurringTransactionRepository.findAll().stream()
                .filter(RecurringTransaction::shouldProcess)
                .toList();
        log.info(recurringTransactions.isEmpty()
                ? "No recurring transactions ready for processing."
                : "Processing recurring transactions."
        );
        for (RecurringTransaction recurringTransaction : recurringTransactions) {
            this.processRecurringTransaction(recurringTransaction);
        }
    }

    private void processRecurringTransaction(RecurringTransaction recurringTransaction) {
        try {
            LocalDate lastTransactionDate = recurringTransaction.getStartDate();
            LocalDate today = LocalDate.now();

            while (lastTransactionDate.isBefore(today)) {
                Transaction transaction = this.createTransactionFromRecurring(recurringTransaction, lastTransactionDate);
                this.transactionRepository.save(transaction);

                lastTransactionDate = recurringTransaction.getNextTransactionDate(lastTransactionDate).orElse(null);
                if (lastTransactionDate == null) {
                    this.recurringTransactionRepository.delete(recurringTransaction);
                    break;
                }
            }
        } catch (Exception e) {
            log.error("Error processing recurring transaction", e);
        }
    }

    private Transaction createTransactionFromRecurring(RecurringTransaction recurringTransaction, LocalDate transactionDate) {
        Transaction transaction = new Transaction();
        transaction.setAccount(recurringTransaction.getAccount());
        transaction.setAmount(recurringTransaction.getAmount());
        transaction.setType(recurringTransaction.getType());
        transaction.setCategory(recurringTransaction.getCategory());
        transaction.setTransactionDate(transactionDate.atStartOfDay());
        return transaction;
    }

    public List<RecurringTransaction> getAllTransactions() {
        return this.recurringTransactionRepository.findAll();
    }

    public Optional<RecurringTransaction> getTransactionById(Long id) {
        return this.recurringTransactionRepository.findById(id);
    }

    public Optional<RecurringTransaction> createTransaction(@Valid RecurringTransaction transaction) {
        return Optional.of(this.recurringTransactionRepository.save(transaction));
    }

    public Optional<RecurringTransaction> updateTransaction(Long id, @Valid RecurringTransaction transaction) {
        return this.recurringTransactionRepository.findById(id)
                .map(existingTransaction -> {
                    existingTransaction.setAmount(transaction.getAmount());
                    existingTransaction.setCategory(transaction.getCategory());
                    existingTransaction.setType(transaction.getType());
                    return this.recurringTransactionRepository.save(existingTransaction);
                });
    }

    public void deleteTransaction(Long id) {
        this.recurringTransactionRepository.deleteById(id);
    }
}
