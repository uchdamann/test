package com.seerbit.test.controllers;

import com.seerbit.test.models.StatisticsResponse;
import com.seerbit.test.models.Transaction;
import com.seerbit.test.models.TransactionRequest;
import com.seerbit.test.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;

@RestController
public class TransactionController {
    @Autowired
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transactions")
    public ResponseEntity<Void> createTransaction(@RequestBody TransactionRequest request) {
        BigDecimal amount;
        Instant timestamp;

        try {
            amount = new BigDecimal(request.getAmount());
            timestamp = Instant.parse(request.getTimestamp());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        if (timestamp.isAfter(Instant.now())) {
            return ResponseEntity.unprocessableEntity().build();
        }

        Transaction transaction = new Transaction(amount, timestamp);
        transactionService.addTransaction(transaction);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/statistics")
    public StatisticsResponse getStatistics() {
        return transactionService.getStatistics();
    }

    @DeleteMapping("/transactions")
    public ResponseEntity<Void> deleteTransactions() {
        transactionService.deleteAllTransactions();
        return ResponseEntity.noContent().build();
    }
}
