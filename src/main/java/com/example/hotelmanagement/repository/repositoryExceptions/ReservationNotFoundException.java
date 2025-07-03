package com.example.hotelmanagement.repository.repositoryExceptions;

public class ReservationNotFoundException extends Exception {

    int reservationId;

    public ReservationNotFoundException(String message) {
        super(message);
        this.reservationId = reservationId;
    }
}
