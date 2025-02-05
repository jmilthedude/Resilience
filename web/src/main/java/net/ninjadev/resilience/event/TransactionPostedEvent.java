package net.ninjadev.resilience.event;

import jakarta.validation.Valid;
import lombok.*;
import net.ninjadev.resilience.entity.transaction.Transaction;
import org.springframework.context.ApplicationEvent;

@Getter
public class TransactionPostedEvent extends ApplicationEvent {

    private final Transaction transaction;

    public TransactionPostedEvent(@Valid Transaction transaction) {
        super(transaction);
        this.transaction = transaction;
    }

}
