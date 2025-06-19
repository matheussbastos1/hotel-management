package service.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

public class Invoice {
    private Long invoiceId;
    private BigDecimal amountSpent;
    private Stay stay;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private boolean isPaid;
    private ArrayList<Payment> payment;

    public Invoice(Long invoiceId, BigDecimal amountSpent, Stay stay, LocalDate issueDate, LocalDate dueDate, boolean isPaid) {
        this.invoiceId = invoiceId;
        this.amountSpent = amountSpent;
        this.stay = stay;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.isPaid = isPaid;
        this.payment = new ArrayList<>();
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public BigDecimal getAmountSpent() {
        return amountSpent;
    }

    public void setAmountSpent(BigDecimal amountSpent) {
        this.amountSpent = amountSpent;
    }

    public Stay getStay() {
        return stay;
    }

    public void setStay(Stay stay) {
        this.stay = stay;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public ArrayList<Payment> getPayment() {
        return payment;
    }

    public void setPayment(ArrayList<Payment> payment) {
        this.payment = payment;
    }
}
