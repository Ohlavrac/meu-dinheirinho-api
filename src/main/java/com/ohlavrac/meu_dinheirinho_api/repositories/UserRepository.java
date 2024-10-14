package com.ohlavrac.meu_dinheirinho_api.repositories;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ohlavrac.meu_dinheirinho_api.domain.entities.users.UsersEntity;

@Repository
public interface UserRepository extends JpaRepository<UsersEntity, UUID> {
    Optional<UsersEntity> findByEmail(String email);
}
