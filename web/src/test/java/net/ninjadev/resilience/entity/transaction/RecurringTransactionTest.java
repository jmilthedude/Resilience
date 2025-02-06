package net.ninjadev.resilience.entity.transaction;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecurringTransactionTest {

    @Test
    void testGetNextTransactionDateDaily() {
        RecurringTransaction transaction = new RecurringTransaction();
        transaction.setFrequency(TransactionFrequency.DAILY);
        transaction.setStartDate(LocalDate.of(2023, 11, 1));

        assertEquals(
                LocalDate.of(2023, 11, 2),
                transaction.getNextTransactionDate(LocalDate.of(2023, 11, 1)).orElseThrow()
        );
    }

    @Test
    void testGetNextTransactionDateWeekly() {
        RecurringTransaction transaction = new RecurringTransaction();
        transaction.setFrequency(TransactionFrequency.WEEKLY);
        transaction.setStartDate(LocalDate.of(2023, 11, 1));

        assertEquals(
                LocalDate.of(2023, 11, 8),
                transaction.getNextTransactionDate(LocalDate.of(2023, 11, 1)).orElseThrow()
        );
    }

    @Test
    void testGetNextTransactionDateMonthly() {
        RecurringTransaction transaction = new RecurringTransaction();
        transaction.setFrequency(TransactionFrequency.MONTHLY);
        transaction.setStartDate(LocalDate.of(2023, 11, 1));

        assertEquals(
                LocalDate.of(2023, 12, 1),
                transaction.getNextTransactionDate(LocalDate.of(2023, 11, 1)).orElseThrow()
        );
    }

    @Test
    void testGetNextTransactionDateYearly() {
        RecurringTransaction transaction = new RecurringTransaction();
        transaction.setFrequency(TransactionFrequency.YEARLY);
        transaction.setStartDate(LocalDate.of(2023, 11, 1));

        assertEquals(
                LocalDate.of(2024, 11, 1),
                transaction.getNextTransactionDate(LocalDate.of(2023, 11, 1)).orElseThrow()
        );
    }

    @Test
    void testGetNextTransactionDateSpecificDays() {
        RecurringTransaction transaction = new RecurringTransaction();
        transaction.setFrequency(TransactionFrequency.SPECIFIC_DAYS);
        transaction.getSpecificDays().addAll(Set.of(5, 15, 25)); // Days of the month

        assertEquals(
                LocalDate.of(2023, 11, 5),
                transaction.getNextTransactionDate(LocalDate.of(2023, 11, 1)).orElseThrow()
        );

        assertEquals(
                LocalDate.of(2023, 11, 15),
                transaction.getNextTransactionDate(LocalDate.of(2023, 11, 5)).orElseThrow()
        );

        assertEquals(
                LocalDate.of(2023, 11, 25),
                transaction.getNextTransactionDate(LocalDate.of(2023, 11, 15)).orElseThrow()
        );

        assertEquals(
                LocalDate.of(2023, 12, 5),
                transaction.getNextTransactionDate(LocalDate.of(2023, 11, 25)).orElseThrow()
        );
    }

    @Test
    void testComputeAllOccurrencesDaily() {
        RecurringTransaction transaction = new RecurringTransaction();
        transaction.setFrequency(TransactionFrequency.DAILY);
        transaction.setStartDate(LocalDate.of(2023, 11, 1));

        List<LocalDate> occurrences = transaction.computeAllOccurrences(
                LocalDate.of(2023, 11, 1),
                LocalDate.of(2023, 11, 5)
        );

        assertEquals(List.of(
                LocalDate.of(2023, 11, 1),
                LocalDate.of(2023, 11, 2),
                LocalDate.of(2023, 11, 3),
                LocalDate.of(2023, 11, 4),
                LocalDate.of(2023, 11, 5)
        ), occurrences);
    }

    @Test
    void testComputeAllOccurrencesMonthlyWithEndDate() {
        RecurringTransaction transaction = new RecurringTransaction();
        transaction.setFrequency(TransactionFrequency.MONTHLY);
        transaction.setStartDate(LocalDate.of(2023, 1, 1));
        transaction.setEndDate(LocalDate.of(2023, 5, 1));

        List<LocalDate> occurrences = transaction.computeAllOccurrences(
                LocalDate.of(2023, 1, 1), // Start of the range
                LocalDate.of(2023, 12, 31) // End of the range
        );

        assertEquals(List.of(
                LocalDate.of(2023, 1, 1), // Include the starting date
                LocalDate.of(2023, 2, 1),
                LocalDate.of(2023, 3, 1),
                LocalDate.of(2023, 4, 1),
                LocalDate.of(2023, 5, 1)  // Include up to endDate
        ), occurrences);

    }

    @Test
    void testComputeAllOccurrencesSpecificDays() {
        RecurringTransaction transaction = new RecurringTransaction();
        transaction.setFrequency(TransactionFrequency.SPECIFIC_DAYS);
        transaction.getSpecificDays().addAll(Set.of(5, 15));
        transaction.setStartDate(LocalDate.of(2023, 11, 1));
        transaction.setEndDate(LocalDate.of(2023, 11, 30));

        List<LocalDate> occurrences = transaction.computeAllOccurrences(
                LocalDate.of(2023, 11, 1),
                LocalDate.of(2023, 11, 30)
        );

        assertEquals(List.of(
                LocalDate.of(2023, 11, 5),
                LocalDate.of(2023, 11, 15)
        ), occurrences);
    }

    @Test
    void testComputeAllOccurrencesNoOccurrences() {
        RecurringTransaction transaction = new RecurringTransaction();
        transaction.setFrequency(TransactionFrequency.DAILY);
        transaction.setStartDate(LocalDate.of(2023, 11, 1));
        transaction.setEndDate(LocalDate.of(2023, 11, 5));

        List<LocalDate> occurrences = transaction.computeAllOccurrences(
                LocalDate.of(2023, 12, 1),
                LocalDate.of(2023, 12, 5)
        );

        assertTrue(occurrences.isEmpty()); // No occurrences since 'startDate' is after range
    }

    @Test
    void testGetNextTransactionDateAfterEndDate() {
        RecurringTransaction transaction = new RecurringTransaction();
        transaction.setFrequency(TransactionFrequency.DAILY);
        transaction.setStartDate(LocalDate.of(2023, 1, 1));
        transaction.setEndDate(LocalDate.of(2023, 1, 5));

        assertTrue(transaction.getNextTransactionDate(LocalDate.of(2023, 1, 6)).isEmpty());
    }
}