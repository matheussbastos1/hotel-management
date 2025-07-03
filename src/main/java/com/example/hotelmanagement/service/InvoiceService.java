package com.example.hotelmanagement.service;

import com.example.hotelmanagement.models.Invoice;
import com.example.hotelmanagement.models.Reservation;


import java.util.List;


public interface InvoiceService {

    List<Invoice> findInvoicesForReservation(Reservation reservation);
}

