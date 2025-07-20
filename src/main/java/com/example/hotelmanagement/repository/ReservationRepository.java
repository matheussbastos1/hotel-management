package com.example.hotelmanagement.repository;

import com.example.hotelmanagement.models.Guest;
import com.example.hotelmanagement.models.Reservation;
import com.example.hotelmanagement.models.ReservationStatus;
import com.example.hotelmanagement.repository.repositoryExceptions.ReservationNotFoundException;
import com.example.hotelmanagement.repository.repositoryExceptions.RoomNotFoundException;


import java.util.List;
import java.util.Optional;


public interface ReservationRepository {

    void addReservation(Reservation reservation);

// ReservationRepository.java
    Optional<Reservation> findActiveByRoomNumber(int roomNumber);
    // Adicione à interface
    List<Reservation> findByStatus(ReservationStatus status);

    List<Reservation> getReservationsByGuest(Guest guest);

    List<Reservation> getAllReservations();

    Reservation findReservationById(int reservationId) throws ReservationNotFoundException;

    void removeReservation(int id) throws ReservationNotFoundException;

    void updateReservation(Reservation reservation) throws RoomNotFoundException, ReservationNotFoundException;
}
