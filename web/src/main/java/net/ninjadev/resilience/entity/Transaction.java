package net.ninjadev.resilience.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @NotNull(message = "Transaction Date cannot be null")
    private LocalDateTime transactionDate;

    @NotNull(message = "Amount cannot be null")
    private double amount;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Type type;

    @NotNull
    private boolean posted; // True if the transaction has been posted

    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull
    private TransactionCategory category;

    public enum Type {
        CREDIT, DEBIT
    }
}
