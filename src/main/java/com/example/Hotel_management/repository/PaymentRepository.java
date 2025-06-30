package com.example.Hotel_management.repository;

import com.example.Hotel_management.repository.repositoryExceptions.PaymentNotFoundException;
import com.example.Hotel_management.models.Payment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository {

    void addPayment(Payment payment);
    Payment findPaymentById(int paymentId) throws PaymentNotFoundException;
    void updatePayment(Payment payment) throws PaymentNotFoundException;
    void deletePaymentById(int paymentId) throws PaymentNotFoundException;
    List<Payment> getAllPayments();
}
