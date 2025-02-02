package net.ninjadev.resilience.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ninjadev.resilience.entity.Account;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddAccountRequest {
    @NotEmpty(message = "Account number cannot be empty")
    private String accountNumber;
    @NotNull(message = "Account type cannot be null")
    private Account.Type type;
}
