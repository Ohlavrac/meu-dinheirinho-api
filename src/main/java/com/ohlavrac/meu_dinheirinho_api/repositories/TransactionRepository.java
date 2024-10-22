package com.ohlavrac.meu_dinheirinho_api.repositories;

import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ohlavrac.meu_dinheirinho_api.domain.entities.transactions.TransactionEntity;

@Repository  
public interface TransactionRepository  extends JpaRepository<TransactionEntity, UUID>{
    //@Query("SELECT t FROM TransactionEntity t WHERE t.users.id =: userID")
    List<TransactionEntity> findByUsersId(UUID usersId);
}
