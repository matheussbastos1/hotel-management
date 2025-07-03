package com.example.hotelmanagement.repository;

import com.example.hotelmanagement.models.Invoice;
import com.example.hotelmanagement.repository.repositoryExceptions.InvoiceNotFoundException;

import java.util.List;



public interface InvoiceRepository {

    void addInvoice(Invoice invoice);
    Invoice findInvoiceById(int invoiceId) throws InvoiceNotFoundException;
    void updateInvoice(Invoice invoice) throws InvoiceNotFoundException;
    void deleteInvoiceById(int invoiceId) throws InvoiceNotFoundException;
    List<Invoice> findAllInvoices();
}
