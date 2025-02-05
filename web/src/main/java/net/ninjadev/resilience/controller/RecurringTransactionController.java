package net.ninjadev.resilience.controller;

import jakarta.validation.Valid;
import net.ninjadev.resilience.entity.transaction.RecurringTransaction;
import net.ninjadev.resilience.response.TransactionResponse;
import net.ninjadev.resilience.service.RecurringTransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions/recurring")
public class RecurringTransactionController {

    private final RecurringTransactionService recurringTransactionService;

    public RecurringTransactionController(RecurringTransactionService recurringTransactionService) {
        this.recurringTransactionService = recurringTransactionService;
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        return ResponseEntity.ok(recurringTransactionService.getAllTransactions().stream()
                .map(TransactionResponse::fromTransaction)
                .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Long id) {
        return recurringTransactionService.getTransactionById(id)
                .map(TransactionResponse::fromTransaction)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()
                );
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@RequestBody @Valid RecurringTransaction transaction) {
        return recurringTransactionService.createTransaction(transaction)
                .map(TransactionResponse::fromTransaction)
                .map(createdTransaction -> ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(@PathVariable Long id, @RequestBody @Valid RecurringTransaction transaction) {
        return recurringTransactionService.updateTransaction(id, transaction)
                .map(TransactionResponse::fromTransaction)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        recurringTransactionService.deleteTransaction(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
