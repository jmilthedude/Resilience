package net.ninjadev.resilience.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ninjadev.resilience.repository.transaction.TransactionRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;


}
