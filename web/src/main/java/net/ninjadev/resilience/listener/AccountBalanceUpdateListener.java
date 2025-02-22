package net.ninjadev.resilience.listener;

import lombok.RequiredArgsConstructor;
import net.ninjadev.resilience.entity.Account;
import net.ninjadev.resilience.entity.transaction.Transaction;
import net.ninjadev.resilience.event.TransactionPostedEvent;
import net.ninjadev.resilience.service.AccountService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountBalanceUpdateListener {
    private final AccountService accountService;

    @EventListener
    public void onTransactionCreated(TransactionPostedEvent event) {
        Transaction transaction = event.getTransaction();
        Account account = transaction.getAccount();

        if (account == null) {
            throw new IllegalStateException("TransactionPostedEvent received a Transaction with a null Account.");
        }

        double signedAmount = getSignedAmount(transaction, account.getType());
        account.setBalance(account.getBalance() + signedAmount);

        accountService.update(account);
    }

    private double getSignedAmount(Transaction transaction, Account.Type accountType) {
        if (accountType == Account.Type.CREDIT) {
            return switch (transaction.getType()) {
                case PAYMENT, TRANSFER_IN -> -Math.abs(transaction.getAmount());
                case PURCHASE, WITHDRAWAL -> Math.abs(transaction.getAmount());
                default -> throw new IllegalStateException("Invalid TransactionType for CREDIT account: %s".formatted(transaction.getType()));
            };
        } else {
            return switch (transaction.getType()) {
                case DEPOSIT, TRANSFER_IN -> Math.abs(transaction.getAmount());
                case WITHDRAWAL, PURCHASE, TRANSFER_OUT -> -Math.abs(transaction.getAmount());
                default -> throw new IllegalStateException("Invalid TransactionType for %s account: %s".formatted(accountType, transaction.getType()));
            };
        }
    }
}
