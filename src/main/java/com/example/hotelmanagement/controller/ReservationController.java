package com.example.hotelmanagement.controller;

import com.example.hotelmanagement.models.Reservation;
import com.example.hotelmanagement.repository.ReservationRepository;
import com.example.hotelmanagement.repository.repositoryExceptions.ReservationNotFoundException;
import com.example.hotelmanagement.repository.impl.ReservationRepositoryImpl;
import com.example.hotelmanagement.repository.repositoryExceptions.RoomNotFoundException;

import java.util.List;
import java.util.Optional;

public class ReservationController extends AbstractController<Reservation> {
    private final ReservationRepository reservationRepository;

    public ReservationController(ReservationRepositoryImpl reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public boolean add(Reservation reservation) {
        reservationRepository.addReservation(reservation);
        return true;
    }

    @Override
    public boolean update(Reservation reservation) throws RoomNotFoundException, ReservationNotFoundException{
        reservationRepository.updateReservation(reservation);
        return true;
    }

    @Override
    public boolean remove(int id) throws ReservationNotFoundException {
        Reservation reservation = reservationRepository.findReservationById(id);
        reservationRepository.removeReservation(reservation);
        return true;
    }

    @Override
    public List<Reservation> findAll() throws ReservationNotFoundException {
        List<Reservation> reservations = reservationRepository.getAllReservations();
        if (reservations.isEmpty()) {
            throw new ReservationNotFoundException("Nenhuma reserva encontrada.");
        }
        return reservations;
    }

    @Override
    public Optional<Reservation> findById(int id) throws ReservationNotFoundException {
        Reservation reservation = reservationRepository.findReservationById(id);
        return Optional.of(reservation);
    }

}
