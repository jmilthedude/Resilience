package net.ninjadev.resilience.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ninjadev.resilience.entity.Account;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {

    private Account.Type type;
    private String accountNumber;
    private double balance;

    public AccountDTO(Account account) {
        this.type = account.getType();
        this.accountNumber = account.getAccountNumber();
        this.balance = account.getBalance();
    }
}
