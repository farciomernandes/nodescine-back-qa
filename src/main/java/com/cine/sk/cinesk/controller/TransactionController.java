package com.cine.sk.cinesk.controller;

import com.cine.sk.cinesk.domain.transaction.*;
import com.cine.sk.cinesk.domain.user.dto.TransactionDTO;
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
    public ResponseEntity<TransactionDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getById(id));
    }

    @GetMapping("/director/summary/by-movie/{id}")
    public ResponseEntity<List<TransactionByMovieDTO>> getDirectorIdByMovie(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getDirectorId(id));
    }

    @GetMapping("/director/summary/{id}")
    public ResponseEntity<SalesTransactionSuDTO> getDirectorIdSummary(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.findSalesResult(id));
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> create(@RequestBody CreateTransactionDTO createTransaction) {
        TransactionResponse created = transactionService.create(createTransaction);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PostMapping("/mock")
    public ResponseEntity<TransactionResponse> createMock(@RequestBody CreateTransactionDTO createTransaction) {
        TransactionResponse created = transactionService.createMock(createTransaction);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<List<TransactionDTO>> getMe() {
        return ResponseEntity.ok(transactionService.getMy());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<TransactionDTO>> getByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getByUserId(id));
    }

    @GetMapping("/summary")
    public ResponseEntity<SalesTransactionSuDTO> summary() {
        return ResponseEntity.ok(transactionService.findSalesResult());
    }

    @GetMapping("/summary/by-movie")
    public ResponseEntity<List<TransactionByMovieDTO>> getTransactionsByMovie() {
        List<TransactionByMovieDTO> transactions = transactionService.getTransactionsByCreatedByAndMovie();
        return ResponseEntity.ok(transactions);
    }
}
