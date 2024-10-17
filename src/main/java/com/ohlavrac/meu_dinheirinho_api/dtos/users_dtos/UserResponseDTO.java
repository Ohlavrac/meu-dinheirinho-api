package com.ohlavrac.meu_dinheirinho_api.dtos.users_dtos;

import java.util.UUID;

import com.ohlavrac.meu_dinheirinho_api.domain.enums.UserRole;

public record UserResponseDTO(
    UUID id,
    String name,
    String email,
    UserRole role
) {

}
