package net.ninjadev.resilience.controller;

import jakarta.validation.Valid;
import net.ninjadev.resilience.entity.transaction.Transaction;
import net.ninjadev.resilience.response.TransactionResponse;
import net.ninjadev.resilience.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions().stream()
                .map(TransactionResponse::fromTransaction)
                .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable("id") Long id) {
        return transactionService.getTransactionById(id)
                .map(TransactionResponse::fromTransaction)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@RequestBody @Valid Transaction transaction) {
        return transactionService.createTransaction(transaction)
                .map(TransactionResponse::fromTransaction)
                .map(createdTransaction -> ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(@PathVariable("id") Long id, @RequestBody @Valid Transaction transaction) {
        return transactionService.updateTransaction(id, transaction)
                .map(TransactionResponse::fromTransaction)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable("id") Long id) {
        transactionService.deleteTransaction(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}/posted")
    public ResponseEntity<Void> markTransactionAsPosted(@PathVariable("id") Long id) {
        return transactionService.markTransactionAsPosted(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

}
