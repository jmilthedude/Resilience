package net.ninjadev.resilience.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ninjadev.resilience.entity.transaction.TransactionCategory;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionCategoryResponse {
    private @NotNull String name;
    private String description;

    public TransactionCategoryResponse(@NotNull TransactionCategory category) {
        this.name = category.getName();
        this.description = category.getDescription();
    }
}
