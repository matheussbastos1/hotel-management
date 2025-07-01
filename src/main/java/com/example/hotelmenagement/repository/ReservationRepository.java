package com.example.hotelmenagement.repository;

import com.example.hotelmenagement.service.repositoryExceptions.ReservationNotFoundException;
import com.example.hotelmenagement.service.repositoryExceptions.RoomNotFoundException;
import com.example.hotelmenagement.models.Guest;
import com.example.hotelmenagement.models.Reservation;

import java.util.List;

public interface ReservationRepository {

    void addReservation(Reservation reservation);

    List<Reservation> getReservationsByGuest(Guest guest);

    List<Reservation> getAllReservations();

    Reservation findReservationById(int reservationId) throws ReservationNotFoundException;

    void removeReservation(Reservation reservation);

    void updateReservation(Reservation reservation) throws RoomNotFoundException, ReservationNotFoundException;
}
