package com.example.Hotel_management.repository;

import com.example.Hotel_management.repository.repositoryExceptions.ReservationNotFoundException;
import com.example.Hotel_management.repository.repositoryExceptions.RoomNotFoundException;
import com.example.Hotel_management.models.Guest;
import com.example.Hotel_management.models.Reservation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository {

    void addReservation(Reservation reservation);

    List<Reservation> getReservationsByGuest(Guest guest);

    List<Reservation> getAllReservations();

    Reservation findReservationById(int reservationId) throws ReservationNotFoundException;

    void removeReservation(Reservation reservation);

    void updateReservation(Reservation reservation) throws RoomNotFoundException, ReservationNotFoundException;
}
