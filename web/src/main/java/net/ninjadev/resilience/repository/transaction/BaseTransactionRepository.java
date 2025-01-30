package net.ninjadev.resilience.repository.transaction;

import net.ninjadev.resilience.entity.Account;
import net.ninjadev.resilience.entity.transaction.Transaction;
import net.ninjadev.resilience.entity.transaction.TransactionCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface BaseTransactionRepository<T extends Transaction, ID extends Serializable> extends JpaRepository<T, ID> {

    Page<T> findByAccount(Account account, Pageable pageable);
    Page<T> findByCategory(TransactionCategory category, Pageable pageable);

}
