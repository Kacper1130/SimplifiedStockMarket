package com.simplifiedstockmarket.exception;

public class OutOfStockException extends RuntimeException {
    public OutOfStockException() {
        super("Stock is currently unavailable in the bank.");
    }
}
