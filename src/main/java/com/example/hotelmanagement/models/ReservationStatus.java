package com.example.hotelmanagement.models;

public enum ReservationStatus {
    BOOKED("Booked"),
    CHECKED_IN("Checked In"),
    CHECKED_OUT("Checked Out"),
    CANCELLED("Cancelled"),
    BEGGAR("beggar");

    private final String status;

    ReservationStatus(String status) {
        this.status = status;
    }
}
