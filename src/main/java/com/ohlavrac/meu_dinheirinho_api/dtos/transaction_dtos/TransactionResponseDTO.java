package com.ohlavrac.meu_dinheirinho_api.dtos.transaction_dtos;

import java.util.UUID;
import java.util.Date;

import com.ohlavrac.meu_dinheirinho_api.domain.enums.TransactionType;

public record TransactionResponseDTO(
    UUID id,
    String title,
    double value,
    TransactionType transaction_type,
    String category,
    Date created_at,
    Date updated_at
) {

}
