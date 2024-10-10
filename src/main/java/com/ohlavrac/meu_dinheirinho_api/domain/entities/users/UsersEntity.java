package com.ohlavrac.meu_dinheirinho_api.domain.entities.users;

import java.util.UUID;
import java.util.List;

import com.ohlavrac.meu_dinheirinho_api.domain.entities.transactions.TransactionEntity;
import com.ohlavrac.meu_dinheirinho_api.domain.enums.UserRole;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "users")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsersEntity {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String email;
    private String password;
    private UserRole role;

    @OneToMany(mappedBy = "users")
    private List<TransactionEntity> transactions;
}