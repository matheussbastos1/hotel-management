package repository;

import service.repositoryExceptions.ReservationNotFoundException;
import service.repositoryExceptions.RoomNotFoundException;
import models.Guest;
import models.Reservation;

import java.util.List;

public interface ReservationRepository {

    void addReservation(Reservation reservation);

    List<Reservation> getReservationsByGuest(Guest guest);

    List<Reservation> getAllReservations();

    Reservation findReservationById(int reservationId) throws ReservationNotFoundException;

    void removeReservation(Reservation reservation);

    void updateReservation(Reservation reservation) throws RoomNotFoundException, ReservationNotFoundException;
}
