package com.simplifiedstockmarket.exception;

public class WalletNotFoundException extends RuntimeException {
    public WalletNotFoundException() {
        super("Wallet not found");
    }
}
