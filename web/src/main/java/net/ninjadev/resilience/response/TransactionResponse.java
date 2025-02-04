package net.ninjadev.resilience.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ninjadev.resilience.entity.transaction.RecurringTransaction;
import net.ninjadev.resilience.entity.transaction.Transaction;
import net.ninjadev.resilience.entity.transaction.TransactionFrequency;
import net.ninjadev.resilience.entity.transaction.TransactionType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private Long id;
    private AccountResponse account;
    private LocalDateTime transactionDate;
    private double amount;
    private TransactionType type;
    private boolean posted;
    private TransactionCategoryResponse category;
    private boolean isRecurring;
    private LocalDate startDate;
    private TransactionFrequency frequency;
    private LocalDate endDate;
    private Set<Integer> specificDays;

    public static <T extends Transaction> TransactionResponse fromTransaction(T transaction) {
        TransactionResponse response = new TransactionResponse();
        response.id = transaction.getId();
        response.account = new AccountResponse(transaction.getAccount());
        response.transactionDate = transaction.getTransactionDate();
        response.amount = transaction.getAmount();
        response.type = transaction.getType();
        response.posted = transaction.isPosted();
        response.category = new TransactionCategoryResponse(transaction.getCategory());
        response.isRecurring = false;

        if (transaction instanceof RecurringTransaction recurringTransaction) {
            response.isRecurring = true;
            response.startDate = recurringTransaction.getStartDate();
            response.frequency = recurringTransaction.getFrequency();
            response.endDate = recurringTransaction.getEndDate();
            response.specificDays = recurringTransaction.getSpecificDays();
        }
        return response;
    }
}
