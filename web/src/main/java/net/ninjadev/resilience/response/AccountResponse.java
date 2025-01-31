package net.ninjadev.resilience.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ninjadev.resilience.entity.Account;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {

    private Account.Type type;
    private String accountNumber;
    private double balance;

    public AccountResponse(Account account) {
        this.type = account.getType();
        this.accountNumber = account.getAccountNumber();
        this.balance = account.getBalance();
    }
}
