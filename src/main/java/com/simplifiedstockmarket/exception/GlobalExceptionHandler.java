package com.simplifiedstockmarket.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler({StockNotFoundException.class, WalletNotFoundException.class})
    public ResponseEntity<ExceptionResponse> handleStockNotFoundException(Exception exception) {
        return new ResponseEntity<>(new ExceptionResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({OutOfStockException.class, StockNotOwnedException.class})
    public ResponseEntity<ExceptionResponse> handleOutOfStockException(Exception exception) {
        return new ResponseEntity<>(new ExceptionResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
