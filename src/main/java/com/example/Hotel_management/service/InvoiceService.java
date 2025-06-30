package com.example.Hotel_management.service;

import com.example.Hotel_management.models.Invoice;
import com.example.Hotel_management.models.Reservation;
import com.example.Hotel_management.models.Stay;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public interface InvoiceService {

    List<Invoice> findInvoicesForReservation(Reservation reservation);
}

