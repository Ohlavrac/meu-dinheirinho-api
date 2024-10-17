package com.ohlavrac.meu_dinheirinho_api.dtos.transaction_dtos;

import com.ohlavrac.meu_dinheirinho_api.domain.enums.TransactionType;

public record TransactionRequestDTO(
    String title,
    double value,
    String category,
    TransactionType transaction_type
) {

}
