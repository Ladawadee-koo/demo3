package com.example.demo3.service;

import com.example.demo3.repository.AccountRepository;
import com.example.demo3.repository.entity.AccountEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Test
    void testGetAccountEntity_whenSuccess_thenReturnAccountEntity(){
        AccountEntity account = AccountEntity.builder().accountId("1").balance(new BigDecimal("100.0")).build();
        when(accountRepository.findById("1")).thenReturn(Optional.ofNullable(account));
        AccountEntity response = accountService.getAccountEntity("1");
        assertNotNull(response);
        assertEquals("1", response.getAccountId());
        assertEquals(new BigDecimal("100.0"), response.getBalance());
        verify(accountRepository, times(1)).findById(eq("1"));
    }

}
