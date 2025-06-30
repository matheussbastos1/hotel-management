package com.example.Hotel_management.repository;

import com.example.Hotel_management.repository.repositoryExceptions.InvoiceNotFoundException;
import com.example.Hotel_management.models.Invoice;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository {

    void addInvoice(Invoice invoice);
    Invoice findInvoiceById(int invoiceId) throws InvoiceNotFoundException;
    void updateInvoice(Invoice invoice) throws InvoiceNotFoundException;
    void deleteInvoiceById(int invoiceId) throws InvoiceNotFoundException;
    List<Invoice> findAllInvoices();
}
