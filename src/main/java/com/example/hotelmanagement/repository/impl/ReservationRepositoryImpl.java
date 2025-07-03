package com.example.hotelmanagement.repository.impl;

import com.example.hotelmanagement.models.Guest;
import com.example.hotelmanagement.models.Reservation;
import com.example.hotelmanagement.repository.ReservationRepository;
import com.example.hotelmanagement.repository.repositoryExceptions.ReservationNotFoundException;
import com.example.hotelmanagement.repository.repositoryExceptions.RoomNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class ReservationRepositoryImpl implements ReservationRepository {

    private final List<Reservation> reservations = new ArrayList<>();

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
