package com.example.hotelmanagement.repository;

import com.example.hotelmanagement.repository.repositoryExceptions.PaymentNotFoundException;
import com.example.hotelmanagement.models.Payment;
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
