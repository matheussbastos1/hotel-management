package com.example.hotelmanagement.service.impl;

import com.example.hotelmanagement.models.Invoice;
import com.example.hotelmanagement.models.Payment;
import com.example.hotelmanagement.models.PaymentMethod;
import com.example.hotelmanagement.models.PaymentStatus;
import com.example.hotelmanagement.repository.InvoiceRepository;
import com.example.hotelmanagement.repository.PaymentRepository;
import com.example.hotelmanagement.repository.repositoryExceptions.InvoiceNotFoundException;
import com.example.hotelmanagement.repository.repositoryExceptions.PaymentNotFoundException;
import com.example.hotelmanagement.service.PaymentService;
import com.example.hotelmanagement.service.serviceExceptions.InvalidOperationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, InvoiceRepository invoiceRepository) {
        this.paymentRepository = paymentRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public void registerPayment(Payment payment) throws InvalidOperationException {
        // Validação completa dos dados
        if (payment == null || payment.getInvoice() == null ||
                payment.getAmount() == null || payment.getPaymentMethod() == null) {
            throw new InvalidOperationException("Dados do pagamento incompletos. O método de pagamento é obrigatório.");
        }

        try {
            // Verifica se a fatura existe
            Invoice invoice = invoiceRepository.findInvoiceById(payment.getInvoice().getInvoiceId());

            // Define a data do pagamento se não estiver definida
            if (payment.getPaymentDate() == null) {
                payment.setPaymentDate(LocalDateTime.now());
            }

            // Define o status inicial com base no método de pagamento
            if (payment.getPaymentStatus() == null) {
                payment.setPaymentStatus(getInitialStatusForPaymentMethod(payment.getPaymentMethod()));
            }

            // Processa o pagamento de acordo com o método escolhido
            processPaymentByMethod(payment);

            // Registra o pagamento
            paymentRepository.addPayment(payment);

            // Atualiza o status do pagamento da fatura
            updateInvoicePaymentStatus(invoice);

        } catch (InvoiceNotFoundException e) {
            throw new InvalidOperationException("Fatura não encontrada: " + e.getMessage());
        }
    }

    // Determina o status inicial com base no método de pagamento
    private PaymentStatus getInitialStatusForPaymentMethod(PaymentMethod method) {
        switch (method) {
            case CASH:
                // Pagamentos em dinheiro são considerados concluídos imediatamente
                return PaymentStatus.COMPLETED;
            case PIX:
                // PIX normalmente é aprovado rapidamente, mas pode exigir confirmação
                return PaymentStatus.APROVED;
            case CREDIT_CARD:
            // Cartões de crédito podem exigir verificação online, então iniciamos como pendente
                return PaymentStatus.PENDING;
            case DEBIT_CARD:
                // Cartões de débito também podem exigir verificação online, então iniciamos como pendente
                return PaymentStatus.PENDING;
            case BANK_TRANSFER:
                // Transferências bancárias podem levar tempo para serem confirmadas, então iniciamos como pendente
                return PaymentStatus.PENDING;
            default:
                // Outros métodos requerem processamento/verificação
                return PaymentStatus.PENDING;
        }
    }

    // Processa o pagamento de acordo com o método
    private void processPaymentByMethod(Payment payment) throws InvalidOperationException {
        switch (payment.getPaymentMethod()) {
            case CASH:
                // Processar pagamento em dinheiro (não requer validação online)
                System.out.println("Processando pagamento em dinheiro no valor de " + payment.getAmount());
                break;

            case CREDIT_CARD:
                // Simular chamada a gateway de pagamento para cartão de crédito
                System.out.println("Processando pagamento com cartão de crédito no valor de " + payment.getAmount());
                // Em um cenário real, aqui seria feita integração com gateway de pagamento
                break;

            case DEBIT_CARD:
                // Simular chamada a gateway de pagamento para cartão de débito
                System.out.println("Processando pagamento com cartão de débito no valor de " + payment.getAmount());
                break;

            case PIX:
                // Simular geração de chave PIX e monitoramento de pagamento
                System.out.println("Gerando chave PIX para pagamento no valor de " + payment.getAmount());
                break;

            case BANK_TRANSFER:
                // Simular geração de dados para transferência bancária
                System.out.println("Gerando dados para transferência bancária no valor de " + payment.getAmount());
                break;

            default:
                throw new InvalidOperationException("Método de pagamento não suportado");
        }
    }

    @Override
    public Payment findPaymentById(int paymentId) throws PaymentNotFoundException {
        return paymentRepository.findPaymentById(paymentId);
    }

    @Override
    public List<Payment> getPaymentsByInvoice(int invoiceId) {
        // Filtra os pagamentos pela fatura
        return paymentRepository.getAllPayments().stream()
                .filter(payment -> payment.getInvoice() != null &&
                        payment.getInvoice().getInvoiceId() == invoiceId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isPaymentCompleted(int invoiceId) {
        try {
            // Busca a fatura
            Invoice invoice = invoiceRepository.findInvoiceById(invoiceId);

            // Verifica se está marcada como paga
            if (invoice.isPaid()) {
                return true;
            }

            // Calcula o total pago
            BigDecimal totalPaid = calculateTotalPaid(invoiceId);

            // Compara com o valor total da fatura
            return totalPaid.compareTo(invoice.getAmountSpent()) >= 0;

        } catch (InvoiceNotFoundException e) {
            return false;
        }
    }

    @Override
    public void updatePaymentStatus(int paymentId, String status) throws PaymentNotFoundException {
        Payment payment = paymentRepository.findPaymentById(paymentId);

        // Atualiza o status com base na string recebida
        try {
            PaymentStatus newStatus = PaymentStatus.valueOf(status.toUpperCase());
            payment.setPaymentStatus(newStatus);
            paymentRepository.updatePayment(payment);

            // Atualiza o status da fatura
            updateInvoicePaymentStatus(payment.getInvoice());

        } catch (IllegalArgumentException e) {
            throw new PaymentNotFoundException("Status de pagamento inválido: " + status);
        }
    }

    // Métodos auxiliares

    private BigDecimal calculateTotalPaid(int invoiceId) {
        List<Payment> payments = getPaymentsByInvoice(invoiceId);

        return payments.stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.COMPLETED ||
                        p.getPaymentStatus() == PaymentStatus.APROVED)
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void updateInvoicePaymentStatus(Invoice invoice) {
        try {
            BigDecimal totalPaid = calculateTotalPaid(invoice.getInvoiceId());

            // Atualiza o status da fatura com base no total pago
            if (totalPaid.compareTo(BigDecimal.ZERO) == 0) {
                invoice.setPaid(false);
            } else if (totalPaid.compareTo(invoice.getAmountSpent()) >= 0) {
                invoice.setPaid(true);
            } else {
                // Pagamento parcial (mantém como não pago, mas os pagamentos estão registrados)
                invoice.setPaid(false);
            }

            invoiceRepository.updateInvoice(invoice);

        } catch (InvoiceNotFoundException e) {
            // Log do erro, mas não propaga exceção para não interromper o fluxo
            System.err.println("Erro ao atualizar status da fatura: " + e.getMessage());
        }
    }
}