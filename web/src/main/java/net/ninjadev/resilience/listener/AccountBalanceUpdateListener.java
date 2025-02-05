package net.ninjadev.resilience.listener;

import lombok.RequiredArgsConstructor;
import net.ninjadev.resilience.entity.Account;
import net.ninjadev.resilience.entity.transaction.Transaction;
import net.ninjadev.resilience.event.TransactionCreatedEvent;
import net.ninjadev.resilience.service.AccountService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountBalanceUpdateListener {
    private final AccountService accountService;

    @EventListener
    public void onTransactionCreated(TransactionCreatedEvent event) {
        Transaction transaction = event.getTransaction();
        Account account = transaction.getAccount();

        if (account == null) {
            throw new IllegalStateException("TransactionCreatedEvent received a Transaction with a null Account.");
        }

        account.setBalance(account.getBalance() + transaction.getAmount());

        accountService.update(account);
    }

}
