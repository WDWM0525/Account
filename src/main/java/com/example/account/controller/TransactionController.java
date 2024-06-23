package com.example.account.controller;

import com.example.account.aop.AccountLock;
import com.example.account.dto.CancelBalance;
import com.example.account.dto.QueryTransactionResponse;
import com.example.account.dto.UseBalance;
import com.example.account.exception.AccountException;
import com.example.account.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 잔액 관련 컨트롤러
 * 1. 잔액 사용
 * 2. 잔액 사용 취소
 * 3. 거래 확인
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    /* ********************************************************************************************
     * POST /transaction/use
     *   파라미터 : 사용자 아이디, 계좌번호, 거래금액
     *   정책 : 사용자 없는 경우, 계좌가 없는 경우, 사용자 아이디와 계좌 소유주가 다른 경우,
     *          계좌가 이미 해지 상태인 경우, 거래금액이 잔액보다 큰 경우,
     *          거래금액이 너무 작거나 큰 경우 실패 응답
     *   성공 응답 : 계좌번호, 거래 결과코드(성공/실패), 거래 아이디, 거래금액, 거래일시
     * ******************************************************************************************** */
    @PostMapping("/transaction/use")
    @AccountLock
    public UseBalance.Response useBalance (
            @Valid @RequestBody UseBalance.Request request
    ) throws InterruptedException {
        try {
            Thread.sleep(3000L);
            return UseBalance.Response.from(
                        transactionService.useBalance(
                                request.getUserId(), request.getAccountNumber(), request.getAmount()));
        } catch (AccountException e) {
            log.error("Failed to use balance.");

            transactionService.saveFailedUseTransaction(
                        request.getAccountNumber()
                    ,   request.getAmount()
            );

            throw e;
        }
    }

    /* ********************************************************************************************
     * POST /transaction/cancel
     *   파라미터 : 거래 아이디, 계좌번호, 취소 요청 금액
     *   정책 : 거래 아이디에 행당하는 거래가 없는 경우, 계좌가 없는 경우, 거래와 계좌가 일치하지 않는 경우
     *         거래금 액과 거래 취소 금액이 다른 경우(부분 취소 불가능) 실패응답
     *      - 1년이 넘은 거래는 사용 취소 불가능
     *      - 해당 계좌에서 거래(사용, 사용 취소)가 진행중일때
     *   성공 응답 : 계좌번호, 거래 결과코드(성공/실패), 거래 아이디, 거래금액, 거래일
     * ******************************************************************************************** */
    @PostMapping("/transaction/cancel")
    @AccountLock
    public CancelBalance.Response useBalance (
            @Valid @RequestBody CancelBalance.Request request
    ) {
        try {
            return CancelBalance.Response.from(
                    transactionService.cancelBalance(
                            request.getTransactionId(), request.getAccountNumber(), request.getAmount()));
        } catch (AccountException e) {
            log.error("Failed to cancel balance.");

            transactionService.saveFailedCancelTransaction(
                        request.getAccountNumber()
                    ,   request.getAmount()
            );

            throw e;
        }
    }

    /* ********************************************************************************************
     * GET /transaction/{transactionId}
     *   파라미터 : 거래 아이디
     *   정책 : 해당 거래 아이디의 거래가 없는 경우 실패 응답
     *   성공 응답 : 계좌번호, 거래종류(잔액 사용, 잔액 사용 취소), 거래 결과 코드(성공/실패)
     *      - 실패한 거래(사용/사용취소)도 거래를 확인할 수 있도록 합니다.
     * ******************************************************************************************** */
    @GetMapping("/transaction/{transactionId}")
    public QueryTransactionResponse queryTransaction(
            @PathVariable String transactionId
    ) {
        return QueryTransactionResponse.from(transactionService.queryTransaction(transactionId));
    }
}
