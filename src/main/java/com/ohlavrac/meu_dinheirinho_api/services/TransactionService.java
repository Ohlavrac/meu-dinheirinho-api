package com.ohlavrac.meu_dinheirinho_api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ohlavrac.meu_dinheirinho_api.domain.entities.transactions.TransactionEntity;
import com.ohlavrac.meu_dinheirinho_api.domain.entities.users.UsersEntity;
import com.ohlavrac.meu_dinheirinho_api.dtos.transaction_dtos.TransactionRequestDTO;
import com.ohlavrac.meu_dinheirinho_api.infra.security.TokenService;
import com.ohlavrac.meu_dinheirinho_api.repositories.TransactionRepository;
import com.ohlavrac.meu_dinheirinho_api.repositories.UserRepository;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    public void createTransaction(String token, TransactionRequestDTO data) {
        TransactionEntity transactionEntity = new TransactionEntity();
        UsersEntity user = getUSer(token);

        transactionEntity.setTitle(data.title());
        transactionEntity.setValue(data.value());
        transactionEntity.setCategory(data.category());
        transactionEntity.setTransaction_type(data.transaction_type());
        transactionEntity.setUsers(user);

        this.transactionRepository.save(transactionEntity);
    }

    private UsersEntity getUSer(String token) {
        String subject = tokenService.validateToken(token);
        return userRepository.findByEmail(subject).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
