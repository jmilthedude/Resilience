package net.ninjadev.resilience.repository.transaction;

import net.ninjadev.resilience.entity.transaction.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends BaseTransactionRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.account.id = :accountId AND t.posted = false")
    List<Transaction> findPendingTransactionsByAccountId(@Param("accountId") Long accountId);
}
