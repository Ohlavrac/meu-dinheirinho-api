package com.ohlavrac.meu_dinheirinho_api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ohlavrac.meu_dinheirinho_api.domain.entities.transactions.TransactionEntity;
import com.ohlavrac.meu_dinheirinho_api.domain.entities.users.UsersEntity;
import com.ohlavrac.meu_dinheirinho_api.domain.enums.TransactionType;
import com.ohlavrac.meu_dinheirinho_api.dtos.transaction_dtos.TransactionRequestDTO;
import com.ohlavrac.meu_dinheirinho_api.dtos.transaction_dtos.TransactionResponseDTO;
import com.ohlavrac.meu_dinheirinho_api.infra.security.TokenService;
import com.ohlavrac.meu_dinheirinho_api.repositories.TransactionRepository;
import com.ohlavrac.meu_dinheirinho_api.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Transactional
    public void createTransaction(String token, TransactionRequestDTO data) {
        TransactionEntity transactionEntity = new TransactionEntity();
        UsersEntity user = getUSer(token);

        transactionEntity.setTitle(data.title());
        transactionEntity.setValue(data.value());
        transactionEntity.setCategory(data.category());
        transactionEntity.setTransaction_type(data.transaction_type());
        transactionEntity.setUsers(user);

        if (data.transaction_type() == TransactionType.INCOMING) {
            this.userRepository.updateBalance(user.getId(), user.getBalance()+data.value());
        } else {
            this.userRepository.updateBalance(user.getId(), user.getBalance()-data.value());
        }

        this.transactionRepository.save(transactionEntity);
    }

    public List<TransactionResponseDTO> getAllUserTransaction(String token) {
        UsersEntity user = getUSer(token);

        //TODO TRANSFORMAR A ESSA CONVERÇÃO E UM MAPPER
        return this.transactionRepository.findByUsersId(user.getId()).stream().map(transaction -> new TransactionResponseDTO(
            transaction.getId(),
            transaction.getTitle(),
            transaction.getValue(),
            transaction.getTransaction_type(),
            transaction.getCategory()
        )).toList();
    }

    private UsersEntity getUSer(String token) {
        String subject = tokenService.validateToken(token);
        return userRepository.findByEmail(subject).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
