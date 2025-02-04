package net.ninjadev.resilience.entity.transaction;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecurringTransaction extends Transaction {
    @NotNull
    private LocalDate startDate; // The date the recurrence starts

    @Enumerated(EnumType.STRING)
    @NotNull
    private TransactionFrequency frequency; // How often the transaction repeats

    @Nullable
    private LocalDate endDate; // Optional end date for recurrence

    @ElementCollection
    @CollectionTable(name = "specific_recurrence_days", joinColumns = @JoinColumn(name = "recurring_transaction_id"))
    @Column(name = "day_of_month")
    private final Set<Integer> specificDays = new HashSet<>();

    @PrePersist
    public void initStartDate() {
        if (startDate == null) {
            startDate = LocalDate.now();
        }
    }

    public Optional<LocalDate> getNextTransactionDate(LocalDate lastTransactionDate) {
        if (endDate != null && lastTransactionDate.isAfter(endDate)) {
            return Optional.empty(); // Recurring transaction has ended
        }

        return switch (frequency) {
            case DAILY -> Optional.of(lastTransactionDate.plusDays(1));
            case WEEKLY -> Optional.of(lastTransactionDate.plusWeeks(1));
            case MONTHLY -> Optional.of(lastTransactionDate.plusMonths(1));
            case YEARLY -> Optional.of(lastTransactionDate.plusYears(1));
            case SPECIFIC_DAYS -> Optional.of(getNextSpecificDay(lastTransactionDate));
        };
    }

    private LocalDate getNextSpecificDay(LocalDate lastTransactionDate) {
        for (int day : specificDays) {
            LocalDate potentialDate = lastTransactionDate.withDayOfMonth(day);
            if (potentialDate.isAfter(lastTransactionDate)) {
                return potentialDate;
            }
        }
        return lastTransactionDate.plusMonths(1).withDayOfMonth(specificDays.iterator().next());
    }
}
