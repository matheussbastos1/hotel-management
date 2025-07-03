package com.example.hotelmanagement.controller;

import com.example.hotelmanagement.models.Payment;
import com.example.hotelmanagement.repository.impl.PaymentRepositoryImpl;
import com.example.hotelmanagement.repository.repositoryExceptions.PaymentNotFoundException;

import java.util.List;
import java.util.Optional;

public class PaymentController extends AbstractController<Payment> {
    private final PaymentRepositoryImpl paymentRepository;

    public PaymentController(PaymentRepositoryImpl paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public boolean add(Payment payment) {
        paymentRepository.addPayment(payment);
        return true;
    }

    @Override
    public boolean update(Payment payment) throws PaymentNotFoundException {
        paymentRepository.updatePayment(payment);
        return true;
    }

    @Override
    public boolean remove(int id) throws PaymentNotFoundException {
        Payment payment = paymentRepository.findPaymentById(id);
        paymentRepository.deletePaymentById(payment.getPaymentId());
        return true;
    }

    @Override
    public List<Payment> findAll() throws PaymentNotFoundException {
        List<Payment> payments = paymentRepository.getAllPayments();
        if (payments.isEmpty()) {
            throw new PaymentNotFoundException("Nenhum pagamento encontrado.");
        }
        return payments;
    }

    @Override
    public Optional<Payment> findById(int id) throws PaymentNotFoundException {
        Payment payment = paymentRepository.findPaymentById(id);
        return Optional.of(payment);
    }

}
