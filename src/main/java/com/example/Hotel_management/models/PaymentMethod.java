package com.example.Hotel_management.models;

public enum PaymentMethod {
    CREDIT_CARD("Credit Card"),
    DEBIT_CARD("Debit Card"),
    PIX("Pix"),
    CASH("Cash"),
    BANK_TRANSFER("Bank Transfer");

    private String method;

     PaymentMethod(String method) {
        this.method = method;
    }
}
