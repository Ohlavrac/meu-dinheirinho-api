package com.ohlavrac.meu_dinheirinho_api.repositories;

import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ohlavrac.meu_dinheirinho_api.domain.entities.transactions.TransactionEntity;

import jakarta.transaction.Transactional;

@Repository  
public interface TransactionRepository  extends JpaRepository<TransactionEntity, UUID>{
    //@Query("SELECT t FROM TransactionEntity t WHERE t.users.id =: userID")
    List<TransactionEntity> findByUsersId(UUID usersId);

    @Modifying
    @Transactional
    @Query("DELETE FROM TransactionEntity t WHERE t.id = :transactionId AND t.users.id = :userId")
    int deleteTransactionByID(@Param("transactionId") UUID transactionId, @Param("userId") UUID userId);
}
