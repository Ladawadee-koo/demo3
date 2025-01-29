package com.example.demo3.controller;

import com.example.demo3.controller.request.DepositRequest;
import com.example.demo3.controller.request.TransferRequest;
import com.example.demo3.controller.request.WithdrawRequest;
import com.example.demo3.factory.ResponseFactory;
import com.example.demo3.repository.entity.AccountEntity;
import com.example.demo3.service.AccountService;
import com.example.demo3.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private ResponseFactory responseFactory;

    @Mock
    private TransactionService transactionService;

    @Mock
    private AccountService accountService;

    @Test
    void testDeposit_whenSuccess_thenReturnSuccess(){
        DepositRequest request = new DepositRequest("1", new BigDecimal("100.00"));
        AccountEntity account = AccountEntity.builder().accountId("1").balance(new BigDecimal("100.0")).build();
        AccountEntity updatedAccount = AccountEntity.builder().accountId("1").balance(new BigDecimal("200.0")).build();
        when(accountService.getAccountEntity("1")).thenReturn(account);
        when(transactionService.depositMoney(account, request.getDepositAmount())).thenReturn(updatedAccount);
        when(responseFactory.success(any(), any())).thenCallRealMethod();
        ResponseEntity response = transactionController.deposit(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(transactionService, times(1)).depositMoney(eq(account), eq(request.getDepositAmount()));
    }

    @Test
    void testDeposit_whenDepositAmountLessThanZero_thenReturnBadRequest(){
        DepositRequest request = new DepositRequest("1", new BigDecimal("-100.00"));
        when(responseFactory.error(any(),any())).thenCallRealMethod();
        ResponseEntity response = transactionController.deposit(request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testDeposit_whenAccountNotFound_thenReturnNotFound(){
        DepositRequest request = new DepositRequest("1", new BigDecimal("100.00"));
        when(accountService.getAccountEntity("1")).thenReturn(null);
        when(responseFactory.error(any(),any())).thenCallRealMethod();
        ResponseEntity response = transactionController.deposit(request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testWithdraw_whenSuccess_thenReturnSuccess(){
        WithdrawRequest request = new WithdrawRequest("1", new BigDecimal("100.00"));
        AccountEntity account = AccountEntity.builder().accountId("1").balance(new BigDecimal("200.0")).build();
        AccountEntity updatedAccount = AccountEntity.builder().accountId("1").balance(new BigDecimal("100.0")).build();
        when(accountService.getAccountEntity("1")).thenReturn(account);
        when(transactionService.withdrawMoney(account, request.getWithdrawAmount())).thenReturn(updatedAccount);
        when(responseFactory.success(any(), any())).thenCallRealMethod();
        ResponseEntity response = transactionController.withdraw(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(transactionService, times(1)).withdrawMoney(eq(account), eq(request.getWithdrawAmount()));
    }

    @Test
    void testWithdraw_whenWithdrawAmountLessThanZero_thenReturnBadRequest(){
        WithdrawRequest request = new WithdrawRequest("1", new BigDecimal("-100.00"));
        when(responseFactory.error(any(),any())).thenCallRealMethod();
        ResponseEntity response = transactionController.withdraw(request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testWithDraw_whenAccountNotFound_thenReturnNotFound(){
        WithdrawRequest request = new WithdrawRequest("1", new BigDecimal("100.00"));
        when(accountService.getAccountEntity("1")).thenReturn(null);
        when(responseFactory.error(any(),any())).thenCallRealMethod();
        ResponseEntity response = transactionController.withdraw(request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testWithDraw_whenInsufficientBalance_thenReturnForbidden(){
        WithdrawRequest request = new WithdrawRequest("1", new BigDecimal("200.00"));
        AccountEntity account = AccountEntity.builder().accountId("1").balance(new BigDecimal("100.0")).build();
        when(accountService.getAccountEntity("1")).thenReturn(account);
        when(responseFactory.error(any(),any())).thenCallRealMethod();
        ResponseEntity response = transactionController.withdraw(request);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testTransfer_whenSuccess_thenReturnSuccess(){
        TransferRequest request = new TransferRequest("1","2", new BigDecimal("100.00"));
        AccountEntity fromAccount = AccountEntity.builder().accountId("1").balance(new BigDecimal("200.0")).build();
        AccountEntity toAccount = AccountEntity.builder().accountId("1").balance(new BigDecimal("50.0")).build();
        when(accountService.getAccountEntity("1")).thenReturn(fromAccount);
        when(accountService.getAccountEntity("2")).thenReturn(toAccount);
        when(responseFactory.success(any(),any())).thenCallRealMethod();
        ResponseEntity response = transactionController.transfer(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(transactionService, times(1)).withdrawMoney(eq(fromAccount), eq(request.getTransferAmount()));
        verify(transactionService, times(1)).depositMoney(eq(toAccount), eq(request.getTransferAmount()));
    }

    @Test
    void testTransaction_whenTransferAmountLessThanZero_thenReturnBadRequest(){
        TransferRequest request = new TransferRequest("1","2", new BigDecimal("-100.00"));
        when(responseFactory.error(any(),any())).thenCallRealMethod();
        ResponseEntity response = transactionController.transfer(request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testTransaction_whenInsufficientBalance_thenReturnForbidden(){
        TransferRequest request = new TransferRequest("1","2", new BigDecimal("100.00"));
        AccountEntity fromAccount = AccountEntity.builder().accountId("1").balance(new BigDecimal("50.0")).build();
        AccountEntity toAccount = AccountEntity.builder().accountId("1").balance(new BigDecimal("200.0")).build();
        when(accountService.getAccountEntity("1")).thenReturn(fromAccount);
        when(accountService.getAccountEntity("2")).thenReturn(toAccount);
        when(responseFactory.error(any(),any())).thenCallRealMethod();

        ResponseEntity response = transactionController.transfer(request);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
