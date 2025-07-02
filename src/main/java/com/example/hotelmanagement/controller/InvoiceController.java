package com.example.hotelmanagement.controller;

import com.example.hotelmanagement.repository.impl.InvoiceRepositoryImpl;
import com.example.hotelmanagement.repository.repositoryExceptions.InvoiceNotFoundException;
import com.example.hotelmanagement.models.Invoice;
import java.util.List;
import java.util.Optional;

public class InvoiceController extends AbstractController<Invoice> {
    private final InvoiceRepositoryImpl invoiceRepository;
    public InvoiceController(InvoiceRepositoryImpl invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public boolean add(Invoice invoice) {
        invoiceRepository.addInvoice(invoice);
        return true;
    }

    @Override
    public boolean update(Invoice invoice) throws InvoiceNotFoundException {
        invoiceRepository.updateInvoice(invoice);
        return true;
    }

    @Override
    public boolean remove(int id) throws InvoiceNotFoundException {
        Invoice invoice = invoiceRepository.findInvoiceById(id);
        int invoiceId = invoice.getInvoiceId();
        invoiceRepository.deleteInvoiceById(invoiceId);
        return true;
    }

    @Override
    public List<Invoice> findAll() throws InvoiceNotFoundException {
        List<Invoice> invoices = invoiceRepository.findAllInvoices();
        if (invoices.isEmpty()) {
            throw new InvoiceNotFoundException("Nenhuma fatura encontrada.");
        }
        return invoices;
    }

    @Override
    public Optional<Invoice> findById(int id) throws InvoiceNotFoundException {
        Invoice invoice = invoiceRepository.findInvoiceById(id);
        return Optional.of(invoice);
    }

}
