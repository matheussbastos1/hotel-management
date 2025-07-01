package com.example.hotelmenagement.models;

public enum RoomStatus {
    AVAILABLE("Available"),
    BOOKED("Booked"),
    MAINTENANCE("Maintenance"),
    OCCUPIED("Occupied");

    private final String status;

    RoomStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
