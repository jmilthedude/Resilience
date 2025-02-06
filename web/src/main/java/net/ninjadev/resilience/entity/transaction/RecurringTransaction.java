package net.ninjadev.resilience.entity.transaction;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecurringTransaction extends Transaction {
    @NotNull
    private LocalDate startDate;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TransactionFrequency frequency;

    @Nullable
    private LocalDate endDate;

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
            return Optional.empty();
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
        List<Integer> sortedDays = specificDays.stream().sorted().toList();

        for (int day : sortedDays) {
            LocalDate potentialDate;
            try {
                potentialDate = lastTransactionDate.withDayOfMonth(Math.min(day, lastTransactionDate.lengthOfMonth()));
            } catch (Exception e) {
                continue;
            }
            if (potentialDate.isAfter(lastTransactionDate)) {
                return potentialDate;
            }
        }
        LocalDate nextMonth = lastTransactionDate.plusMonths(1);
        int firstDay = sortedDays.get(0);
        return nextMonth.withDayOfMonth(Math.min(firstDay, nextMonth.lengthOfMonth()));
    }

    private boolean isSpecificDay(LocalDate date) {
        return specificDays.contains(date.getDayOfMonth());
    }

    public List<LocalDate> computeAllOccurrences(LocalDate start, LocalDate end) {
        if(start == null || end == null) {
            throw new IllegalArgumentException("Start and end dates cannot be null");
        }

        List<LocalDate> occurrences = new ArrayList<>();

        LocalDate currentDate = !start.isBefore(this.getStartDate()) ? start : this.getStartDate();

        if (this.frequency == TransactionFrequency.SPECIFIC_DAYS && !isSpecificDay(currentDate)) {
            currentDate = getNextTransactionDate(currentDate).orElse(null);
        }

        while (currentDate != null && !currentDate.isAfter(end)) {
            if(this.getEndDate() != null && currentDate.isAfter(this.getEndDate())) break;
            occurrences.add(currentDate);
            currentDate = this.getNextTransactionDate(currentDate).orElse(null);
        }
        return occurrences;
    }



}
