package com.ohlavrac.meu_dinheirinho_api.mappers;

import java.util.List;

import com.ohlavrac.meu_dinheirinho_api.domain.entities.transactions.TransactionEntity;
import com.ohlavrac.meu_dinheirinho_api.dtos.transaction_dtos.TransactionResponseDTO;

public class TransactionsMapper {

    public List<TransactionResponseDTO> transactionsEntityToDTO(List<TransactionEntity> transactions) {

        return transactions.stream().map(transaction -> new TransactionResponseDTO(
            transaction.getId(),
            transaction.getTitle(),
            transaction.getValue(),
            transaction.getTransaction_type(),
            transaction.getCategory(),
            transaction.getCreated_at(),
            transaction.getUpdated_at()
        )).toList();
    }

    public TransactionResponseDTO entityToDto (TransactionEntity transaction) {
        return new TransactionResponseDTO(
            transaction.getId(),
            transaction.getTitle(),
            transaction.getValue(),
            transaction.getTransaction_type(),
            transaction.getCategory(),
            transaction.getCreated_at(),
            transaction.getUpdated_at()
        );
    }
}
