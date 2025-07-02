package com.example.hotelmanagement.repository.impl;

import com.example.hotelmanagement.repository.PaymentRepository;
import com.example.hotelmanagement.repository.repositoryExceptions.PaymentNotFoundException;
import com.example.hotelmanagement.models.Payment;

import java.util.ArrayList;
import java.util.List;

public class PaymentRepositoryImpl implements PaymentRepository {

    ArrayList<Payment> payments = new ArrayList<>();

    @Override
    public void addPayment(Payment payment) {
        this.payments.add(payment);
    }

    @Override
    public Payment findPaymentById(int paymentId) throws PaymentNotFoundException {
        for (Payment payment : payments) {
            if (payment.getPaymentId() == paymentId) {
                return payment;
            }
        }
       throw new PaymentNotFoundException("Pagamento com o ID " + paymentId + " não encontrado.");
    }

    @Override
    public void updatePayment(Payment payment) throws PaymentNotFoundException {
        for (int i = 0; i < payments.size(); i++) {
            if (payments.get(i).getPaymentId() == payment.getPaymentId()) {
                payments.set(i, payment);
                return;
            }
        }
        throw new PaymentNotFoundException("Pagamento com o ID" + payment.getPaymentId() + " não encontrado.");
    }

    @Override
    public void deletePaymentById(int paymentId) throws PaymentNotFoundException {
        boolean removed = this.payments.removeIf(payment -> payment.getPaymentId() == paymentId);
        if (!removed) {
            throw new PaymentNotFoundException("Pagamento com o ID " + paymentId + " não encontrado para exclusão.");
        }
    }

    @Override
    public List<Payment> getAllPayments() {
        return new ArrayList<>(this.payments);
    }
}
