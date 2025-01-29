package net.ninjadev.resilience.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
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
    private Frequency frequency; // How often the transaction repeats

    @Nullable
    private LocalDate endDate; // Optional end date for recurrence

    @ElementCollection
    @CollectionTable(name = "specific_recurrence_days", joinColumns = @JoinColumn(name = "recurring_transaction_id"))
    @Column(name = "day_of_month")
    private final Set<Integer> specificDays = new HashSet<>();

    public enum Frequency {
        DAILY, WEEKLY, MONTHLY, YEARLY, SPECIFIC_DAYS
    }

    public LocalDate getNextTransactionDate(LocalDate lastTransactionDate) {
        return switch (frequency) {
            case DAILY -> lastTransactionDate.plusDays(1);
            case WEEKLY -> lastTransactionDate.plusWeeks(1);
            case MONTHLY -> lastTransactionDate.plusMonths(1);
            case YEARLY -> lastTransactionDate.plusYears(1);
            case SPECIFIC_DAYS -> getNextSpecificDay(lastTransactionDate);
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
