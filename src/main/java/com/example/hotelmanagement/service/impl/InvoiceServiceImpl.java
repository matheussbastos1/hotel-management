package com.example.hotelmanagement.service.impl;

import com.example.hotelmanagement.models.Invoice;
import com.example.hotelmanagement.models.Reservation;
import com.example.hotelmanagement.models.Stay;
import com.example.hotelmanagement.repository.InvoiceRepository;
import com.example.hotelmanagement.service.InvoiceService;

import java.util.List;
import java.util.stream.Collectors;

public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public List<Invoice> findInvoicesForReservation(Reservation reservation) {
        // Esta é uma implementação simplificada. Em um sistema real,
        // haveria uma relação direta entre Invoice e Reservation
        List<Invoice> allInvoices = invoiceRepository.findAllInvoices();
        return allInvoices.stream()
                .filter(invoice -> {
                    Stay stay = invoice.getStay();
                    return stay != null &&
                            stay.getReservation() != null &&
                            stay.getReservation().getReservationId().equals(reservation.getReservationId());
                })
                .collect(Collectors.toList());
    }
}

