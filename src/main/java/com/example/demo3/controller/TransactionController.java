package com.example.demo3.controller;

import com.example.demo3.controller.request.DepositRequest;
import com.example.demo3.controller.request.TransferRequest;
import com.example.demo3.controller.request.WithdrawRequest;
import com.example.demo3.factory.ResponseFactory;
import com.example.demo3.repository.entity.AccountEntity;
import com.example.demo3.service.AccountService;
import com.example.demo3.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final AccountService accountService;
    private final TransactionService transactionService;
    private final ResponseFactory responseFactory;

    @PostMapping("/transaction/deposit")
    ResponseEntity deposit (@RequestBody DepositRequest request){
        log.info("start deposit money");
        if(request.getDepositAmount().compareTo(new BigDecimal("0.0")) <= 0){
            log.error("deposit amount must be greater than 0");
            return responseFactory.error(HttpStatus.BAD_REQUEST, "Invalid Information");
        }
        AccountEntity account = accountService.getAccountEntity(request.getAccountId());
        if (account == null){
            log.error("account not found");
            return responseFactory.error(HttpStatus.NOT_FOUND, "Not Found");
        }
        AccountEntity updatedAccount = transactionService.depositMoney(account, request.getDepositAmount());
        String response = "deposit success, total balance : " + updatedAccount.getBalance();
        return responseFactory.success(response, String.class);
    }

    @PostMapping("/transaction/withdraw")
    public ResponseEntity withdraw(@RequestBody WithdrawRequest request){
        log.info("start withdraw money");
        if(request.getWithdrawAmount().compareTo(new BigDecimal("0.0")) <= 0) {
            log.error("withdraw amount must grater than 0");
            return responseFactory.error(HttpStatus.BAD_REQUEST, "Invalid Information");
        }
        AccountEntity account = accountService.getAccountEntity(request.getAccountId());
        if (account == null){
            log.error("account not found");
            return responseFactory.error(HttpStatus.NOT_FOUND, "Not Found");
        }
        if (account.getBalance().compareTo(request.getWithdrawAmount()) <= 0){
            log.error("insufficient balance");
            return responseFactory.error(HttpStatus.FORBIDDEN, "Insufficient Balance");
        }
        AccountEntity updatedAccount = transactionService.withdrawMoney(account, request.getWithdrawAmount());
        String response = "withdraw success, total balance : " + updatedAccount.getBalance();
        return responseFactory.success(response, String.class);
    }

    @PostMapping("/transaction/transfer")
    public ResponseEntity transfer(@RequestBody TransferRequest request){

        if(request.getTransferAmount().compareTo(new BigDecimal("0.0")) <= 0){
            log.error("transfer amount must grater than 0");
            return responseFactory.error(HttpStatus.BAD_REQUEST, "Invalid Information");
        }
        AccountEntity fromAccount = accountService.getAccountEntity(request.getFromAccountId());
        AccountEntity toAccount = accountService.getAccountEntity(request.getToAccountId());

        if(fromAccount == null || toAccount == null){
            log.error("account not found");
            return responseFactory.error(HttpStatus.NOT_FOUND, "Not Found");
        }
        if(fromAccount.getBalance().compareTo(request.getTransferAmount()) < 0){
            log.error("insufficient balance");
            return responseFactory.error(HttpStatus.FORBIDDEN, "Insufficient Balance");
        }
        transactionService.withdrawMoney(fromAccount, request.getTransferAmount());
        transactionService.depositMoney(toAccount, request.getTransferAmount());
        return responseFactory.success("transfer success", String.class);
    }
}
