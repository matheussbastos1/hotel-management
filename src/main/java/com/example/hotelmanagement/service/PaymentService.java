package com.example.hotelmanagement.service;

import com.example.hotelmanagement.models.Payment;
import com.example.hotelmanagement.repository.repositoryExceptions.PaymentNotFoundException;
import com.example.hotelmanagement.service.serviceExceptions.InvalidOperationException;

import java.util.List;

public interface PaymentService {

    void registerPayment(Payment payment) throws InvalidOperationException;

    Payment findPaymentById(int paymentId) throws PaymentNotFoundException;

    List<Payment> getPaymentsByInvoice(int invoiceId);

    boolean isPaymentCompleted(int invoiceId);

    void updatePaymentStatus(int paymentId, String status) throws PaymentNotFoundException;
}