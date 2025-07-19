package com.example.hotelmanagement.repository.impl;

import com.example.hotelmanagement.models.Guest;
import com.example.hotelmanagement.models.Reservation;
import com.example.hotelmanagement.models.ReservationStatus;
import com.example.hotelmanagement.repository.ReservationRepository;
import com.example.hotelmanagement.repository.repositoryExceptions.ReservationNotFoundException;
import com.example.hotelmanagement.repository.repositoryExceptions.RoomNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReservationRepositoryImpl implements ReservationRepository {

    private final List<Reservation> reservations = new ArrayList<>();

   // ReservationRepositoryImpl.java
   @Override
   public Optional<Reservation> findActiveByRoomNumber(int roomNumber) {
       for (Reservation reservation : reservations) {
           if (reservation.getRoom().getRoomNumber() == roomNumber
               && reservation.getStatus() == ReservationStatus.CHECKED_IN) {
               return Optional.of(reservation);
           }
       }
       return Optional.empty();
   }
   @Override
   public List<Reservation> findByStatus(ReservationStatus status) {
       return reservations.stream()
           .filter(reservation -> reservation.getStatus() == status)
           .collect(Collectors.toList());
   }


    @Override
    public void addReservation(Reservation reservation) {
       this.reservations.add(reservation);
    }

    @Override
    public List<Reservation> getReservationsByGuest(Guest guest) {

        return new ArrayList<>(this.reservations);
    }

    @Override
    public List<Reservation> getAllReservations() {

        return new ArrayList<>(this.reservations);
    }

    @Override
    public Reservation findReservationById(int reservationId) throws ReservationNotFoundException {
        for (Reservation reservation : reservations) {
            if (reservation.getReservationId() == reservationId) {
                return reservation;
            }
        }

        throw new ReservationNotFoundException("Reserva com o ID " + reservationId + " não encontrada.");
    }

    @Override
    public void removeReservation(Reservation reservation) {
        this.reservations.remove(reservation);
    }

    @Override
    public void updateReservation(Reservation reservation) throws RoomNotFoundException, ReservationNotFoundException {
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).getReservationId() == reservation.getReservationId()) {
                reservations.set(i, reservation);
                return;
            }
        }

        throw new ReservationNotFoundException("Reserva com o ID " + reservation.getReservationId() + " não encontrada para atualização.");
    }
}
