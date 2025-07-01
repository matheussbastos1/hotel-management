package com.example.hotelmenagement.repository;

import org.springframework.stereotype.Repository;
import com.example.hotelmenagement.service.repositoryExceptions.InvoiceNotFoundException;
import com.example.hotelmenagement.models.Invoice;

import java.util.List;

@Repository

public interface InvoiceRepository {

    void addInvoice(Invoice invoice);
    Invoice findInvoiceById(int invoiceId) throws InvoiceNotFoundException;
    void updateInvoice(Invoice invoice) throws InvoiceNotFoundException;
    void deleteInvoiceById(int invoiceId) throws InvoiceNotFoundException;
    List<Invoice> findAllInvoices();
}
