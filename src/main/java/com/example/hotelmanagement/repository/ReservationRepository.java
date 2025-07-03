package com.example.hotelmanagement.repository;

import com.example.hotelmanagement.models.Guest;
import com.example.hotelmanagement.models.Reservation;
import com.example.hotelmanagement.repository.repositoryExceptions.ReservationNotFoundException;
import com.example.hotelmanagement.repository.repositoryExceptions.RoomNotFoundException;

import java.util.List;


public interface ReservationRepository {

    void addReservation(Reservation reservation);

    List<Reservation> getReservationsByGuest(Guest guest);

    List<Reservation> getAllReservations();

    Reservation findReservationById(int reservationId) throws ReservationNotFoundException;

    void removeReservation(Reservation reservation);

    void updateReservation(Reservation reservation) throws RoomNotFoundException, ReservationNotFoundException;
}
