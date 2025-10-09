package com.cine.sk.cinesk.controller;

import com.cine.sk.cinesk.domain.transaction.CreateTransactionDTO;
import com.cine.sk.cinesk.domain.transaction.Transaction;
import com.cine.sk.cinesk.domain.transaction.TransactionResponse;
import com.cine.sk.cinesk.domain.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> get(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Transaction> create(@RequestBody CreateTransactionDTO createTransaction) {
        Transaction created = transactionService.create(createTransaction);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PostMapping("/mock")
    public ResponseEntity<TransactionResponse> createMock(@RequestBody CreateTransactionDTO createTransaction) {
        TransactionResponse created = transactionService.createMock(createTransaction);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<List<Transaction>> getMe() {
        return ResponseEntity.ok(transactionService.getMy());
    }
}
