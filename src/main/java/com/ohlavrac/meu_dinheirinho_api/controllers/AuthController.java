package com.ohlavrac.meu_dinheirinho_api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ohlavrac.meu_dinheirinho_api.dtos.users_dtos.LoginRequestDTO;
import com.ohlavrac.meu_dinheirinho_api.dtos.users_dtos.LoginResponseDTO;
import com.ohlavrac.meu_dinheirinho_api.dtos.users_dtos.RegisterRequestDTO;
import com.ohlavrac.meu_dinheirinho_api.services.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService service;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO data) {
        LoginResponseDTO loginResponseDTO = service.login(data);
        return ResponseEntity.ok(loginResponseDTO);
    }


    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequestDTO data) {

        if (service.registerNewUser(data) == true) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }

        return ResponseEntity.badRequest().build();
    }
}
