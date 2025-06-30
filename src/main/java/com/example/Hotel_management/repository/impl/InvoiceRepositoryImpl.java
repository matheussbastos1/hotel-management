package com.example.Hotel_management.repository.impl;

import com.example.Hotel_management.repository.InvoiceRepository;
import com.example.Hotel_management.repository.repositoryExceptions.InvoiceNotFoundException;
import com.example.Hotel_management.models.Invoice;

import java.util.ArrayList;
import java.util.List;

public class InvoiceRepositoryImpl implements InvoiceRepository {

    ArrayList<Invoice> invoices = new ArrayList<>();

    @Override
    public void addInvoice(Invoice invoice) {
        this.invoices.add(invoice);
    }

    @Override
    public Invoice findInvoiceById(int invoiceId) throws InvoiceNotFoundException {
        for (Invoice invoice : invoices) {
            if (invoice.getInvoiceId() == invoiceId) {
                return invoice;
            }
        }
        throw new InvoiceNotFoundException("Invoice with ID " + invoiceId + " not found.");
    }

    @Override
    public void updateInvoice(Invoice invoice) throws InvoiceNotFoundException {
            for(int i = 0; i < invoices.size(); i++) {
                if (invoices.get(i).getInvoiceId() == invoice.getInvoiceId()) {
                    invoices.set(i, invoice);
                    return;
                }
            }
    }

    @Override
    public void deleteInvoiceById(int invoiceId) throws InvoiceNotFoundException {
        boolean removed = this.invoices.removeIf(invoice -> invoice.getInvoiceId() == invoiceId);
        if (!removed) {
            throw new InvoiceNotFoundException("Fatura com o ID " + invoiceId + " não encontrada para exclusão.");
        }
    }

    @Override
    public List<Invoice> findAllInvoices() {
        return new ArrayList<>(invoices);
    }
}
