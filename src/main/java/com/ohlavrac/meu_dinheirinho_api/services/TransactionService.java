package com.ohlavrac.meu_dinheirinho_api.services;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ohlavrac.meu_dinheirinho_api.domain.entities.transactions.TransactionEntity;
import com.ohlavrac.meu_dinheirinho_api.domain.entities.users.UsersEntity;
import com.ohlavrac.meu_dinheirinho_api.domain.enums.Periods;
import com.ohlavrac.meu_dinheirinho_api.domain.enums.TransactionType;
import com.ohlavrac.meu_dinheirinho_api.dtos.transaction_dtos.TransactionRequestDTO;
import com.ohlavrac.meu_dinheirinho_api.dtos.transaction_dtos.TransactionResponseDTO;
import com.ohlavrac.meu_dinheirinho_api.infra.security.TokenService;
import com.ohlavrac.meu_dinheirinho_api.mappers.TransactionsMapper;
import com.ohlavrac.meu_dinheirinho_api.repositories.TransactionRepository;
import com.ohlavrac.meu_dinheirinho_api.repositories.UserRepository;
import com.ohlavrac.meu_dinheirinho_api.utils.VerifyPeriod;

import jakarta.transaction.Transactional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TransactionsMapper transactionsMapper;

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
        List<TransactionEntity> transactions = this.transactionRepository.findByUsersId(user.getId());
        List<TransactionResponseDTO> transactionsResponseDto = this.transactionsMapper.transactionsEntityToDTO(transactions);
        return transactionsResponseDto;
    }

    public TransactionResponseDTO getUserTransactionById(String token, UUID id) {
        UsersEntity user = getUSer(token);

        TransactionEntity transactionEntity = this.transactionRepository.getUserTransactionById(id, user.getId()).orElseThrow(() -> new RuntimeException("Transaction not foud"));

        return this.transactionsMapper.entityToDto(transactionEntity);
    }

    public List<TransactionResponseDTO> getAllUserTransactionsByType(String token, TransactionType type) {
        UsersEntity user = getUSer(token);
        List<TransactionEntity> transactionsEntity = this.transactionRepository.findByUsersId(user.getId());
        List<TransactionResponseDTO> transctionsResponse = new ArrayList<TransactionResponseDTO>();

        for (int index = 0; index < transactionsEntity.size(); index++) {
            if (transactionsEntity.get(index).getTransaction_type() == type) {
                transctionsResponse.add(this.transactionsMapper.entityToDto(transactionsEntity.get(index)));
            } else {
                continue;
            }
        }

        return transctionsResponse;
    }

    @Transactional
    public boolean deleteTransaction(String token, UUID transactionID) {
        UsersEntity user = getUSer(token);

        TransactionEntity transactionEntity = this.transactionRepository.findById(transactionID).orElseThrow(() -> new RuntimeException("Transaction not found"));
        double value = transactionEntity.getValue();
        TransactionType type = transactionEntity.getTransaction_type();

        int deleteRows = this.transactionRepository.deleteTransactionByID(transactionID, user.getId());

        if (deleteRows != 0) {
            if (type == TransactionType.EXPENSE) {
                System.out.println("OPA MEU");
                this.userRepository.updateBalance(user.getId(), user.getBalance()+value);
            } else {
                this.userRepository.updateBalance(user.getId(), user.getBalance()-value);
            }
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public boolean updateTransaction(String token, UUID id, TransactionRequestDTO data) {
        UsersEntity user = getUSer(token);

        TransactionEntity transactionEntity = this.transactionRepository.getReferenceById(id);
        //TransactionEntity transactionEntity = this.transactionRepository.getUserTransactionById(data.id(), user.getId()).orElseThrow(() -> new RuntimeException("Transaction not foud"));

        if (transactionEntity.getUsers().getId() == user.getId()) {
            transactionEntity.setTitle(data.title().isEmpty() ? transactionEntity.getTitle() : data.title());

            if (data.transaction_type() != transactionEntity.getTransaction_type() || data.value() != transactionEntity.getValue()) {
                if (data.transaction_type() == TransactionType.EXPENSE) {
                    this.userRepository.updateBalance(user.getId(), (user.getBalance() - transactionEntity.getValue()) - data.value());
                } else {
                    this.userRepository.updateBalance(user.getId(), (user.getBalance() - transactionEntity.getValue()) + data.value());
                }

                transactionEntity.setTransaction_type(data.transaction_type());
            }

            transactionEntity.setValue(data.value() != transactionEntity.getValue() ? data.value() : transactionEntity.getValue());
            transactionEntity.setCategory(data.category().isEmpty() ? transactionEntity.getCategory() : data.category());

            this.transactionRepository.save(transactionEntity);
            return true;
        } else {
            return false;
        }
    }

    public List<TransactionResponseDTO> getTransactionsByPeriodAndType(String token, Periods period, TransactionType type) {
        List<TransactionResponseDTO> transctionsResponse = new ArrayList<TransactionResponseDTO>();
        LocalDate date = LocalDate.now();
        LocalDate periodDateTime = new VerifyPeriod().verifyPeriod(period, date);

        UsersEntity user = getUSer(token);

        List<TransactionEntity> transactionEntity = this.transactionRepository.getTransactionsByTime(
            java.sql.Date.valueOf(periodDateTime), user.getId()).orElseThrow(() -> new RuntimeException("Transactions not found"));
        
        for (int index = 0; index <= transactionEntity.size(); index++) {
            if (transactionEntity.get(index).getTransaction_type() == type) {
                transctionsResponse.add(this.transactionsMapper.entityToDto(transactionEntity.get(index)));
            }
        }

        return transctionsResponse;
    }

    public List<TransactionResponseDTO> getTransactionsByPeriod(String token, Periods period ) {
        LocalDate date = LocalDate.now();
        LocalDate periodDateTime = new VerifyPeriod().verifyPeriod(period, date);

        UsersEntity user = getUSer(token);

        List<TransactionEntity> transactionEntity = this.transactionRepository.getTransactionsByTime(
            java.sql.Date.valueOf(periodDateTime), user.getId()).orElseThrow(() -> new RuntimeException("Transactions not found"));
        
        return this.transactionsMapper.transactionsEntityToDTO(transactionEntity);
    }

    private UsersEntity getUSer(String token) {
        String subject = tokenService.validateToken(token);
        return userRepository.findByEmail(subject).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
