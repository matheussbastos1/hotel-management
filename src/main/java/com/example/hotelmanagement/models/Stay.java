package com.example.hotelmanagement.models;

import lombok.*;
import java.time.LocalDate;

//Essa classe representa uma estadia de um hóspede em um hotel, vinculada a uma reserva específica.

@Getter
@Setter
@NoArgsConstructor
public class Stay {
    private Long stayId;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String status;
    private Reservation reservation;
    private Guest guest;

    @Builder
    public Stay(Long stayId, @NonNull Reservation reservation, @NonNull String status) {
        this.stayId = stayId;
        this.reservation = reservation;
        this.status = status;

        // A anotação @NonNull já faz a verificação de nulo automaticamente
        // e lança NullPointerException se for nulo
        this.room = reservation.getRoom();
        this.guest = reservation.getGuest();
        this.checkInDate = reservation.getCheckInDate();
        this.checkOutDate = reservation.getCheckOutDate();
    }
}