package com.example.demo3.service;

import com.example.demo3.repository.AccountRepository;
import com.example.demo3.repository.entity.AccountEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountEntity getAccountEntity (String accountId){
        log.info("start get account by id : [{}]", accountId);
        return  accountRepository.findById(accountId).orElse(null);
    }

}
