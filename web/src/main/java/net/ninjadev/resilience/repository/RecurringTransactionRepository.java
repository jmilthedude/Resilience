package net.ninjadev.resilience.repository;

import net.ninjadev.resilience.entity.RecurringTransaction;
import net.ninjadev.resilience.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecurringTransactionRepository extends JpaRepository<RecurringTransaction, Long> {
}
