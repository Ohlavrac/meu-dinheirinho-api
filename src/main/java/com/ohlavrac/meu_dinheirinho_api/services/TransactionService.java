package com.ohlavrac.meu_dinheirinho_api.services;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

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

    public TransactionResponseDTO getUserTransactionById(String token, UUID id) {
        UsersEntity user = getUSer(token);

        TransactionEntity transactionEntity = this.transactionRepository.getUserTransactionById(id, user.getId());

        return new TransactionResponseDTO(
            transactionEntity.getId(),
            transactionEntity.getTitle(),
            transactionEntity.getValue(),
            transactionEntity.getTransaction_type(),
            transactionEntity.getCategory()
        );
    }

    public List<TransactionResponseDTO> getAllUserTransactionsByType(String token, TransactionType type) {
        UsersEntity user = getUSer(token);
        List<TransactionEntity> transactionsEntity = this.transactionRepository.findByUsersId(user.getId());
        List<TransactionResponseDTO> transctionsResponse = new ArrayList<TransactionResponseDTO>();

        for (int index = 0; index < transactionsEntity.size(); index++) {
            if (transactionsEntity.get(index).getTransaction_type() == type) {
                transctionsResponse.add(new TransactionResponseDTO(
                    transactionsEntity.get(index).getId(),
                    transactionsEntity.get(index).getTitle(),
                    transactionsEntity.get(index).getValue(),
                    transactionsEntity.get(index).getTransaction_type(),
                    transactionsEntity.get(index).getCategory()
                ));
            } else {
                continue;
            }
        }

        return transctionsResponse;
    }

    public boolean deleteTransaction(String token, UUID transactionID) {
        UsersEntity user = getUSer(token);

        TransactionEntity transactionEntity = this.transactionRepository.findById(transactionID).orElseThrow(() -> new RuntimeException("Transaction not found"));
        double value = transactionEntity.getValue();
        TransactionType type = transactionEntity.getTransaction_type();

        int deleteRows = this.transactionRepository.deleteTransactionByID(transactionID, user.getId());

        if (deleteRows != 0) {
            if (type == TransactionType.EXPENSE) {
                this.userRepository.updateBalance(user.getId(), user.getBalance()+value);
            } else {
                this.userRepository.updateBalance(user.getId(), user.getBalance()-value);
            }
            return true;
        } else {
            return false;
        }
    }

    private UsersEntity getUSer(String token) {
        String subject = tokenService.validateToken(token);
        return userRepository.findByEmail(subject).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
