package net.ninjadev.resilience.repository;

import net.ninjadev.resilience.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
