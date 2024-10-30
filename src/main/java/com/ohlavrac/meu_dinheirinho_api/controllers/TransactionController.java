package com.ohlavrac.meu_dinheirinho_api.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ohlavrac.meu_dinheirinho_api.domain.enums.Periods;
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
    public ResponseEntity<List<TransactionResponseDTO>> getUserTransactions(
        @RequestHeader("Authorization") String token,
        @RequestParam(required = false) TransactionType type,
        @RequestParam(required = false) Periods period
    ) {
        
        if (type != null) {
            if (period == null) {
                return ResponseEntity.ok(this.transactionService.getAllUserTransactionsByType(token, type));
            } else {
                return ResponseEntity.ok(this.transactionService.getTransactionsByPeriodAndType(token, period, type));
            }
        } else if (type == null && period != null) {
            return ResponseEntity.ok(this.transactionService.getTransactionsByPeriod(token, period));
        } else {
            return ResponseEntity.ok(this.transactionService.getAllUserTransaction(token));
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

    @PatchMapping("/update")
    public ResponseEntity<Void> updateTransaction(@RequestHeader("Authorization") String token, @RequestBody TransactionRequestDTO data, @RequestParam(required = true) UUID id) {
        boolean isUpdated = this.transactionService.updateTransaction(token, id, data);
        if (isUpdated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
