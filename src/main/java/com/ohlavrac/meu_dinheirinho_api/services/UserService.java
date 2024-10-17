package com.ohlavrac.meu_dinheirinho_api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ohlavrac.meu_dinheirinho_api.domain.entities.users.UsersEntity;
import com.ohlavrac.meu_dinheirinho_api.dtos.users_dtos.UserResponseDTO;
import com.ohlavrac.meu_dinheirinho_api.infra.security.TokenService;
import com.ohlavrac.meu_dinheirinho_api.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private TokenService tokenService;

    public UserResponseDTO getUserInfo(String token) {
        UsersEntity user = getUSer(token);

        UserResponseDTO responseDTO = new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getRole());

        return responseDTO;
    }

    private UsersEntity getUSer(String token) {
        String subject = tokenService.validateToken(token);
        return repository.findByEmail(subject).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
