package service.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//Essa classe representa um pagamento realizado por um hóspede para uma fatura específica.

public class Payment {
    private int paymentId;
    private Invoice invoice;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;

    public Payment(int paymentId, Invoice invoice, BigDecimal amount, PaymentMethod paymentMethod) {
        this.paymentId = paymentId;
        this.invoice = invoice;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = LocalDateTime.now();
        this.paymentStatus = PaymentStatus.PENDING; // Status inicial
    }

    public int getPaymentId() {
        return paymentId;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
}