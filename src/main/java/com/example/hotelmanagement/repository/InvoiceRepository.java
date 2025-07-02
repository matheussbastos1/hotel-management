package com.example.hotelmanagement.repository;

import org.springframework.stereotype.Repository;
import com.example.hotelmanagement.repository.repositoryExceptions.InvoiceNotFoundException;
import com.example.hotelmanagement.models.Invoice;

import java.util.List;

@Repository

public interface InvoiceRepository {

    void addInvoice(Invoice invoice);
    Invoice findInvoiceById(int invoiceId) throws InvoiceNotFoundException;
    void updateInvoice(Invoice invoice) throws InvoiceNotFoundException;
    void deleteInvoiceById(int invoiceId) throws InvoiceNotFoundException;
    List<Invoice> findAllInvoices();
}
