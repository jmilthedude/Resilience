package net.ninjadev.resilience.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.ninjadev.resilience.entity.transaction.RecurringTransaction;
import net.ninjadev.resilience.entity.transaction.Transaction;
import net.ninjadev.resilience.repository.transaction.RecurringTransactionRepository;
import net.ninjadev.resilience.repository.transaction.TransactionRepository;
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
            try {
                if(recurringTransaction.getStartDate().isAfter(LocalDate.now())) {
                    continue;
                }

                LocalDate lastTransactionDate = recurringTransaction.getStartDate();
                LocalDate today = LocalDate.now();

                while (lastTransactionDate.isBefore(today)) {

                    Transaction transaction = new Transaction();
                    transaction.setAccount(recurringTransaction.getAccount());
                    transaction.setAmount(recurringTransaction.getAmount());
                    transaction.setType(recurringTransaction.getType());
                    transaction.setCategory(recurringTransaction.getCategory());
                    transaction.setTransactionDate(lastTransactionDate.atStartOfDay());

                    transactionRepository.save(transaction);

                    // Get the next transaction date
                    lastTransactionDate = recurringTransaction.getNextTransactionDate(lastTransactionDate).orElse(null);
                    if (lastTransactionDate == null) {
                        this.recurringTransactionRepository.delete(recurringTransaction);
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Error processing recurring transaction: " + e.getMessage());
            }
        }
    }
}
