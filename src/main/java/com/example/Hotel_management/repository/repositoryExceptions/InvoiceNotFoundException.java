package com.example.Hotel_management.repository.repositoryExceptions;

public class InvoiceNotFoundException extends Exception {
        Long invoiceId;
    public InvoiceNotFoundException(String message) {

        super(message);
        this.invoiceId = invoiceId;
    }
}
