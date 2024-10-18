package com.ohlavrac.meu_dinheirinho_api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ohlavrac.meu_dinheirinho_api.dtos.transaction_dtos.TransactionRequestDTO;
import com.ohlavrac.meu_dinheirinho_api.dtos.transaction_dtos.TransactionResponseDTO;
import com.ohlavrac.meu_dinheirinho_api.services.TransactionService;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Void> createTransaction(@RequestHeader("Authorization") String token, @RequestBody TransactionRequestDTO data) {

        this.transactionService.createTransaction(token, data);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/all")
    public ResponseEntity<List<TransactionResponseDTO>> getUserTransactions(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(this.transactionService.getAllUserTransaction(token));
    }
}
