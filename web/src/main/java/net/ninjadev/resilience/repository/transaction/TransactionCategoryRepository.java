package net.ninjadev.resilience.repository.transaction;

import jakarta.validation.constraints.NotNull;
import net.ninjadev.resilience.entity.transaction.TransactionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionCategoryRepository extends JpaRepository<TransactionCategory, Long> {

    Optional<TransactionCategory> getByName(@NotNull String name);

}
