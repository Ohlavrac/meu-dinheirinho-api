package com.ohlavrac.meu_dinheirinho_api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.ohlavrac.meu_dinheirinho_api.domain.entities.users.UserDetailsImpl;
import com.ohlavrac.meu_dinheirinho_api.domain.entities.users.UsersEntity;
import com.ohlavrac.meu_dinheirinho_api.dtos.users_dtos.LoginRequestDTO;
import com.ohlavrac.meu_dinheirinho_api.dtos.users_dtos.LoginResponseDTO;
import com.ohlavrac.meu_dinheirinho_api.dtos.users_dtos.RegisterRequestDTO;
import com.ohlavrac.meu_dinheirinho_api.infra.security.SecurityConfig;
import com.ohlavrac.meu_dinheirinho_api.infra.security.TokenService;
import com.ohlavrac.meu_dinheirinho_api.repositories.UserRepository;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository repository;

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private TokenService tokenService;

    public boolean registerNewUser(RegisterRequestDTO data) {
        UsersEntity user = new UsersEntity();

        if (repository.findByEmail(data.email()).isPresent()) return false;

        String encodedPassword = securityConfig.passwordEncoder().encode(data.password());

        user.setName(data.name());
        user.setEmail(data.email());
        user.setPassword(encodedPassword);
        user.setRole(data.role());

        this.repository.save(user);

        return true;
    }

    public LoginResponseDTO login(LoginRequestDTO data) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(data.email(), data.password());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();

        return new LoginResponseDTO(data.email(), tokenService.generateToken(userDetailsImpl));
    }
}
