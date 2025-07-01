package com.example.hotelmenagement.repository;

import com.example.hotelmenagement.service.repositoryExceptions.PaymentNotFoundException;
import com.example.hotelmenagement.models.Payment;

import java.util.List;

public interface PaymentRepository {

    void addPayment(Payment payment);
    Payment findPaymentById(int paymentId) throws PaymentNotFoundException;
    void updatePayment(Payment payment) throws PaymentNotFoundException;
    void deletePaymentById(int paymentId) throws PaymentNotFoundException;
    List<Payment> getAllPayments();
}
