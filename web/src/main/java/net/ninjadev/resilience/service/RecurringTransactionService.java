package net.ninjadev.resilience.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ninjadev.resilience.entity.transaction.RecurringTransaction;
import net.ninjadev.resilience.entity.transaction.Transaction;
import net.ninjadev.resilience.repository.transaction.RecurringTransactionRepository;
import net.ninjadev.resilience.repository.transaction.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecurringTransactionService {

    private final TransactionRepository transactionRepository;
    private final RecurringTransactionRepository recurringTransactionRepository;

    @Transactional
    public void process() {
        List<RecurringTransaction> recurringTransactions = this.recurringTransactionRepository.findAll();

        for (RecurringTransaction recurringTransaction : recurringTransactions) {
            this.processRecurringTransaction(recurringTransaction);
        }
    }

    private void processRecurringTransaction(RecurringTransaction recurringTransaction) {
        try {
            if (recurringTransaction.getStartDate().isAfter(LocalDate.now())) {
                return;
            }

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

    private Transaction createTransactionFromRecurring(
            RecurringTransaction recurringTransaction, LocalDate transactionDate) {
        Transaction transaction = new Transaction();
        transaction.setAccount(recurringTransaction.getAccount());
        transaction.setAmount(recurringTransaction.getAmount());
        transaction.setType(recurringTransaction.getType());
        transaction.setCategory(recurringTransaction.getCategory());
        transaction.setTransactionDate(transactionDate.atStartOfDay());
        return transaction;
    }

}
