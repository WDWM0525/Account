package com.example.account.controller;

import com.example.account.domain.Account;
import com.example.account.dto.AccountInfo;
import com.example.account.dto.CreateAccount;
import com.example.account.dto.DeleteAccount;
import com.example.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    /* ********************************************************************************************
    * POST / account
    *   파라미터 : 사용자 아이디, 초기 잔액
    *   정책 : 사용자가 없는 경우, 계좌가 10개 (사용자당 최대 보유 가능 계좌수)인 경우 실패 응답
    *   성공 응답 : 사용자 아이디, 계좌번호, 등록일시
    * ******************************************************************************************** */
    @PostMapping("/account")
    public CreateAccount.Response createAccount(
            @RequestBody @Valid CreateAccount.Request request
    ) {
        return CreateAccount.Response.from(
                accountService.createAccount(
                        request.getUserId(),
                        request.getInitialBalance()
                )
        );
    }

    /* ********************************************************************************************
     * DELETE /account
     *   파라미터 : 사용자 아이디, 계좌번호
     *   정책 : 사용자 또는 계좌가 없는 경우, 사용자 아이디와 계좌 소유주가 다른경우,
     *          계좌가 이미 해지 상태인 경우, 잔액이 있는 경우 실패 응답
     *   성공 응답 : 사용자 아이디, 계좌번호, 해지일시
     * ******************************************************************************************** */
    @DeleteMapping("/account")
    public DeleteAccount.Response createAccount(
            @RequestBody @Valid DeleteAccount.Request request
    ) {
        return DeleteAccount.Response.from(
                accountService.deleteAccount(
                        request.getUserId(),
                        request.getAccountNumber()
                )
        );
    }

    /* ********************************************************************************************
     * GET /account?user_id={userId}
     *   파라미터 : 사용자 아이디
     *   정책 : 사용자 없는 경우 실패 응답
     *   성공 응답 : List<계좌번호, 잔액> 구조로 응답(사용 중인 계좌만)
     * ******************************************************************************************** */
    @GetMapping("/account")
    public List<AccountInfo> getAccountsByUserId(
            @RequestParam("user_id") Long userId
    ) {
        return accountService.getAccountsByUserId(userId)
                .stream().map(accountDto ->
                        AccountInfo.builder()
                        .accountNumber(accountDto.getAccountNumber())
                        .balance(accountDto.getBalance())
                        .build())
                .collect(Collectors.toList());
    }

    @GetMapping("/account/{id}")
    public Account getAccount(
            @PathVariable Long id){
        return accountService.getAccount(id);
    }

}
