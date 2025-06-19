package service.models;

import java.time.LocalDate;

public class Stay {
    private int stayId;
    private int roomNumber;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String status;
    private Reservation reservation;
    private Guest guest;

    public Stay(int stayId, Reservation reservation, String status) {
        this.stayId = stayId;
        this.reservation = reservation;
        this.roomNumber = reservation.getRoom().getRoomNumber(); // Usando o getter de Room via Reservation
        this.checkInDate = reservation.getCheckInDate();
        this.checkOutDate = reservation.getCheckOutDate();
        this.status = status;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}