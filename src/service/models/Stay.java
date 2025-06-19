package service.models;

import service.models.Guest;
import service.models.Reservation;
import service.models.Room;

import java.time.LocalDate;

//Essa classe representa uma estadia de um hóspede em um hotel, vinculada a uma reserva específica.

public class Stay {
    private Long stayId;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String status;
    private Reservation reservation;
    private Guest guest;

    public Stay(Long stayId, Reservation reservation, String status) {
        this.stayId = stayId;
        this.reservation = reservation;
        this.status = status;


        if (reservation != null) {
            this.room = reservation.getRoom();
            this.guest = reservation.getGuest();
            this.checkInDate = reservation.getCheckInDate();
            this.checkOutDate = reservation.getCheckOutDate();
        }
    }


    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Long getStayId() {
        return stayId;
    }

    public void setStayId(Long stayId) {
        this.stayId = stayId;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }
}