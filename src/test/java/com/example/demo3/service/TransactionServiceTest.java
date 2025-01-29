package com.example.demo3.service;

import com.example.demo3.repository.AccountRepository;
import com.example.demo3.repository.entity.AccountEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private AccountRepository accountRepository;

    @Test
    void testDepositMoney_whenSuccess_thenReturnAccountEntity(){
        AccountEntity account = AccountEntity.builder().accountId("1").balance(new BigDecimal("200.00")).build();
        BigDecimal depositAmount = new BigDecimal("100.00");
        when(accountRepository.save(account)).thenReturn(AccountEntity.builder().accountId("1").balance(new BigDecimal("300.00")).build());
        AccountEntity response = transactionService.depositMoney(account, depositAmount);
        assertEquals("1", response.getAccountId());
        assertEquals(new BigDecimal("300.00"), response.getBalance());
        verify(accountRepository, times(1)).save(eq(account));
    }

    @Test
    void testWithdrawMoney_whenSuccess_thenReturnAccountEntity(){
        AccountEntity account = AccountEntity.builder().accountId("1").balance(new BigDecimal("200.00")).build();
        BigDecimal withdrawAmount = new BigDecimal("100.00");
        when(accountRepository.save(account)).thenReturn(AccountEntity.builder().accountId("1").balance(new BigDecimal("100.00")).build());
        AccountEntity response = transactionService.withdrawMoney(account, withdrawAmount);
        assertEquals("1", response.getAccountId());
        assertEquals(new BigDecimal("100.00"), response.getBalance());
        verify(accountRepository, times(1)).save(eq(account));
    }
}
