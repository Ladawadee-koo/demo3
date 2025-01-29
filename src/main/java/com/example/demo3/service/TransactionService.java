package com.example.demo3.service;

import com.example.demo3.repository.AccountRepository;
import com.example.demo3.repository.entity.AccountEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;

    public AccountEntity depositMoney (AccountEntity account, BigDecimal depositAmount){
        log.info("start deposit money");
        BigDecimal balance = account.getBalance();
        account.setBalance(balance.add(depositAmount));
        return accountRepository.save(account);
    }

    public AccountEntity withdrawMoney (AccountEntity account, BigDecimal withdrawAmount){
        log.info("start withdraw money");
        BigDecimal balance = account.getBalance();
        account.setBalance(balance.subtract(withdrawAmount));
        return accountRepository.save(account);
    }
}
