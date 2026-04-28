package com.simplifiedstockmarket.exception;

public class StockNotOwnedException extends RuntimeException {
    public StockNotOwnedException() {
        super("Stock not owned");
    }
}
