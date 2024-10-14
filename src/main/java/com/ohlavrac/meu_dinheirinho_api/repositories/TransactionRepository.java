package com.ohlavrac.meu_dinheirinho_api.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ohlavrac.meu_dinheirinho_api.domain.entities.transactions.TransactionEntity;

@Repository
public interface TransactionRepository  extends JpaRepository<TransactionEntity, UUID>{

}
