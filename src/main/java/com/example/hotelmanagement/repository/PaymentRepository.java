package com.example.hotelmanagement.repository;

import com.example.hotelmanagement.models.Payment;
import com.example.hotelmanagement.repository.repositoryExceptions.PaymentNotFoundException;

import java.util.List;


public interface PaymentRepository {

    void addPayment(Payment payment);
    Payment findPaymentById(int paymentId) throws PaymentNotFoundException;
    void updatePayment(Payment payment) throws PaymentNotFoundException;
    void deletePaymentById(int paymentId) throws PaymentNotFoundException;
    List<Payment> getAllPayments();
}
