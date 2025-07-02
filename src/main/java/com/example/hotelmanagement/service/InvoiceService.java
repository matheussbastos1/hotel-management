package com.example.hotelmanagement.service;

import com.example.hotelmanagement.models.Invoice;
import com.example.hotelmanagement.models.Reservation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InvoiceService {

    List<Invoice> findInvoicesForReservation(Reservation reservation);
}

