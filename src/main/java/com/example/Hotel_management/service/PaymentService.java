package com.example.Hotel_management.service;

import com.example.Hotel_management.models.Payment;
import com.example.Hotel_management.repository.repositoryExceptions.PaymentNotFoundException;
import com.example.Hotel_management.service.serviceExceptions.InvalidOperationException;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {

    void registerPayment(Payment payment) throws InvalidOperationException;

    Payment findPaymentById(int paymentId) throws PaymentNotFoundException;

    List<Payment> getPaymentsByInvoice(int invoiceId);

    boolean isPaymentCompleted(int invoiceId);

    void updatePaymentStatus(int paymentId, String status) throws PaymentNotFoundException;
}