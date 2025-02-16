package net.ninjadev.resilience.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ninjadev.resilience.entity.Account;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {

    private long id;
    private String name;
    private Account.Type type;
    private String accountNumber;
    private double balance;

    public AccountResponse(Account account) {
        this.id = account.getId();
        this.name = account.getName();
        this.type = account.getType();
        this.accountNumber = account.getAccountNumber();
        this.balance = account.getBalance();
    }
}
