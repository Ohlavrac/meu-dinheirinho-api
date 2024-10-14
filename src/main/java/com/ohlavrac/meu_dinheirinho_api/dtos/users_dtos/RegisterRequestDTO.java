package com.ohlavrac.meu_dinheirinho_api.dtos.users_dtos;

import com.ohlavrac.meu_dinheirinho_api.domain.enums.UserRole;

public record RegisterRequestDTO(
    String name,
    String email,
    String password,
    UserRole role
) {

}
