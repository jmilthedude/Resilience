package net.ninjadev.resilience.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ninjadev.resilience.entity.transaction.RecurringTransaction;
import net.ninjadev.resilience.entity.transaction.Transaction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private Long id;
    private AccountDTO account;
    private LocalDateTime transactionDate;
    private double amount;
    private Transaction.Type type;
    private boolean posted;
    private TransactionCategoryDTO category;
    private boolean isRecurring;
    private LocalDate startDate;
    private RecurringTransaction.Frequency frequency;
    private LocalDate endDate;
    private Set<Integer> specificDays;

    public <T extends Transaction> TransactionDTO(T transaction) {
        this.id = transaction.getId();
        this.account = new AccountDTO(transaction.getAccount());
        this.transactionDate = transaction.getTransactionDate();
        this.amount = transaction.getAmount();
        this.type = transaction.getType();
        this.posted = transaction.isPosted();
        this.category = new TransactionCategoryDTO(transaction.getCategory());
        this.isRecurring = false;

        if (transaction instanceof RecurringTransaction recurringTransaction) {
            this.isRecurring = true;
            this.startDate = recurringTransaction.getStartDate();
            this.frequency = recurringTransaction.getFrequency();
            this.endDate = recurringTransaction.getEndDate();
            this.specificDays = recurringTransaction.getSpecificDays();
        }
    }
}
