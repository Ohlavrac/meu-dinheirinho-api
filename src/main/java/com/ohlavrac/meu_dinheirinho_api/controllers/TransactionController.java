package com.ohlavrac.meu_dinheirinho_api.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ohlavrac.meu_dinheirinho_api.domain.enums.TransactionType;
import com.ohlavrac.meu_dinheirinho_api.dtos.transaction_dtos.TransactionRequestDTO;
import com.ohlavrac.meu_dinheirinho_api.dtos.transaction_dtos.TransactionResponseDTO;
import com.ohlavrac.meu_dinheirinho_api.services.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Void> createTransaction(@RequestHeader("Authorization") String token, @RequestBody TransactionRequestDTO data) {

        this.transactionService.createTransaction(token, data);

        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity<List<TransactionResponseDTO>> getUserTransactions(@RequestHeader("Authorization") String token, @RequestParam(required = false) TransactionType type) {
        
        if (type == null) {
            return ResponseEntity.ok(this.transactionService.getAllUserTransaction(token));
        } else {
            return ResponseEntity.ok(this.transactionService.getAllUserTransactionsByType(token, type));
        }
    }

    @GetMapping("/info")
    public ResponseEntity<TransactionResponseDTO> getUserTransactionByID(@RequestHeader("Authorization") String token, @RequestParam(required = true) UUID id) {
        return ResponseEntity.ok(this.transactionService.getUserTransactionById(token, id));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteTransactionById(@RequestHeader("Authorization") String token, @RequestParam(required = true) UUID id) {
        this.transactionService.deleteTransaction(token, id);
        return ResponseEntity.ok().build();
    }
}
