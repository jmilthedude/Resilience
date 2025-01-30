package net.ninjadev.resilience.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ninjadev.resilience.entity.transaction.TransactionCategory;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionCategoryDTO {
    private @NotNull String name;
    private String description;

    public TransactionCategoryDTO(@NotNull TransactionCategory category) {
        this.name = category.getName();
        this.description = category.getDescription();
    }
}
