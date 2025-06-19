package service.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//Essa classe representa uma fatura gerada para uma estadia de um hóspede.

public class Invoice {
    private int invoiceId;
    private Stay stay;
    private Map<String, BigDecimal> charges;
    private BigDecimal amountSpent;
    private boolean paid;
    private List<Payment> payments;

    public Invoice(int invoiceId, Stay stay, Map<String, BigDecimal> charges) {
        this.invoiceId = invoiceId;
        this.stay = stay;
        this.charges = charges;
        this.amountSpent = charges.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        this.paid = false;
        this.payments = new ArrayList<>();
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public BigDecimal getAmountSpent() {
        return amountSpent;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public Stay getStay() {
        return stay;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void addPayment(Payment payment) {
        this.payments.add(payment);
    }
}