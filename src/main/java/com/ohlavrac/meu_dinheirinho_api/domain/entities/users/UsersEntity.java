package com.ohlavrac.meu_dinheirinho_api.domain.entities.users;

import java.io.Serializable;
import java.util.UUID;

import com.ohlavrac.meu_dinheirinho_api.domain.enums.UserRole;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "users")
@Entity()
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsersEntity implements Serializable {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;
}