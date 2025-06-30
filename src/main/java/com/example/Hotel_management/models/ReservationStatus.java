package com.example.Hotel_management.models;

public enum ReservationStatus {
    BOOKED("Booked"),
    CHECKED_IN("Checked In"),
    CHECKED_OUT("Checked Out"),
    CANCELLED("Cancelled");

    private final String status;

    ReservationStatus(String status) {
        this.status = status;
    }
}
