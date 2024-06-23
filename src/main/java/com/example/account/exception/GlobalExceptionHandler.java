package com.example.account.exception;

import com.example.account.dto.ErrorResponse;
import com.example.account.type.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.account.type.ErrorCode.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountException.class)
    public ErrorResponse handleAccountException(AccountException e) {
        log.error("{} is occured.", e.getErrorCode());

        return new ErrorResponse(e.getErrorCode(), e.getErrorMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException is occured.", e);

        return new ErrorResponse(
                    INVALID_REQUSET
                ,   INVALID_REQUSET.getDescription()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorResponse handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("DataIntegrityViolationException is occured.", e);

        return new ErrorResponse(
                    INVALID_REQUSET
                ,   INVALID_REQUSET.getDescription()
        );
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(AccountException e) {
        log.error("Exception is occured.", e);

        return new ErrorResponse(
                    INTERNAL_SERVER_ERROR
                ,   INTERNAL_SERVER_ERROR.getDescription()
        );
    }
}
