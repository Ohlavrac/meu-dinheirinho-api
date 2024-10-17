package com.ohlavrac.meu_dinheirinho_api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ohlavrac.meu_dinheirinho_api.dtos.users_dtos.UserResponseDTO;
import com.ohlavrac.meu_dinheirinho_api.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public ResponseEntity<UserResponseDTO> getUserInfo(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(this.userService.getUserInfo(token));
    }
}
