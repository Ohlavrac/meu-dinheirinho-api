package com.ohlavrac.meu_dinheirinho_api.repositories;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ohlavrac.meu_dinheirinho_api.domain.entities.users.UsersEntity;

@Repository
public interface UserRepository extends JpaRepository<UsersEntity, UUID> {
    Optional<UsersEntity> findByEmail(String email);

    @Modifying
    @Query("UPDATE UsersEntity u SET u.balance = :balance WHERE u.id = :id")
    int updateBalance(@Param("id") UUID id, @Param("balance") double balance);
}
