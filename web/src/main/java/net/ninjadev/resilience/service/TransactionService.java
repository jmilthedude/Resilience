package net.ninjadev.resilience.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.ninjadev.resilience.entity.RecurringTransaction;
import net.ninjadev.resilience.entity.Transaction;
import net.ninjadev.resilience.repository.RecurringTransactionRepository;
import net.ninjadev.resilience.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final RecurringTransactionRepository recurringTransactionRepository;

    @Transactional
    public void processRecurringTransactions() {
        List<RecurringTransaction> recurringTransactions = this.recurringTransactionRepository.findAll();

        for (RecurringTransaction recurringTransaction : recurringTransactions) {
            LocalDate lastTransactionDate = recurringTransaction.getStartDate();
            LocalDate today = LocalDate.now();

            while (lastTransactionDate.isBefore(today)) {

                Transaction transaction = new Transaction();
                transaction.setAccount(recurringTransaction.getAccount());
                transaction.setAmount(recurringTransaction.getAmount());
                transaction.setType(recurringTransaction.getType());
                transaction.setCategory(recurringTransaction.getCategory());
                transaction.setTransactionDate(lastTransactionDate.atStartOfDay());
                transaction.setPosted(true);

                transactionRepository.save(transaction);

                // Get the next transaction date
                lastTransactionDate = recurringTransaction.getNextTransactionDate(lastTransactionDate);
            }
        }
    }
}
