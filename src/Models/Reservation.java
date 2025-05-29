package Models;

import java.time.LocalDate;

public class Reservation {
    private int reservationId;
    private String guestName;
    private String guestEmail;
    private String guestPhone;
    private String guestAddress;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String status;
    private Guest guest;

    public Reservation(Guest guest, Room room, LocalDate checkInDate, LocalDate checkOutDate, String status) {
        this.reservationId = reservationId++;
        this.guestName = guest.getGuestName(); // Obtendo o nome do hóspede
        this.guestEmail = guest.getGuestEmail(); // Obtendo o email do hóspede
        this.guestPhone = guest.getGuestPhone(); // Obtendo o telefone do hóspede
        this.guestAddress = guest.getGuestAddress(); // Obtendo o endereço do hóspede
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = status;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getGuestEmail() {
        return guestEmail;
    }

    public String getGuestPhone() {
        return guestPhone;
    }

    public String getGuestAddress() {
        return guestAddress;
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

    public Guest getGuest(Guest guest) {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }
}