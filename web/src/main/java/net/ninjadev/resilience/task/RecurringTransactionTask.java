package net.ninjadev.resilience.task;

import lombok.RequiredArgsConstructor;
import net.ninjadev.resilience.service.RecurringTransactionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecurringTransactionTask {
    private final RecurringTransactionService recurringTransactionService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void processRecurringTransactions() {
        this.recurringTransactionService.process();
    }
}
