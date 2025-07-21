package com.example.hotelmanagement.repository.impl;

import com.example.hotelmanagement.models.Guest;
import com.example.hotelmanagement.models.Reservation;
import com.example.hotelmanagement.models.ReservationStatus;
import com.example.hotelmanagement.repository.ReservationRepository;
import com.example.hotelmanagement.repository.repositoryExceptions.ReservationNotFoundException;
import com.example.hotelmanagement.repository.repositoryExceptions.RoomNotFoundException;
import com.example.hotelmanagement.util.DataPersistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReservationRepositoryImpl implements ReservationRepository {

    private static ReservationRepositoryImpl instance;
    private static final String RESERVATIONS_FILE = "reservations";

    public ReservationRepositoryImpl() {
    }

    public static synchronized ReservationRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new ReservationRepositoryImpl();
        }
        return instance;
    }

    @Override
    public void addReservation(Reservation reservation) {
        List<Reservation> reservations = DataPersistence.loadFromFile(RESERVATIONS_FILE);
        if (reservations == null) {
            reservations = new ArrayList<>();
        }

        // Define um ID único para a nova reserva
        long newId = reservations.stream()
                .mapToLong(r -> r.getReservationId() != null ? r.getReservationId() : 0L)
                .max()
                .orElse(0L) + 1;

        reservation.setReservationId(newId);
        reservations.add(reservation);
        DataPersistence.saveToFile(reservations, RESERVATIONS_FILE);
    }

    @Override
    public Optional<Reservation> findActiveByRoomNumber(int roomNumber) {
        List<Reservation> reservations = DataPersistence.loadFromFile(RESERVATIONS_FILE);
        if (reservations == null) return Optional.empty();

        return reservations.stream()
                .filter(r -> r.getRoom() != null && r.getRoom().getRoomNumber() == roomNumber)
                .filter(r -> r.getStatus() == ReservationStatus.CHECKED_IN)
                .findFirst();
    }

    @Override
    public List<Reservation> findByStatus(ReservationStatus status) {
        List<Reservation> reservations = DataPersistence.loadFromFile(RESERVATIONS_FILE);
        if (reservations == null) return new ArrayList<>();

        return reservations.stream()
                .filter(r -> r.getStatus() == status)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    @Override
    public List<Reservation> getReservationsByGuest(Guest guest) {
        List<Reservation> reservations = DataPersistence.loadFromFile(RESERVATIONS_FILE);
        if (reservations == null) return new ArrayList<>();

        return reservations.stream()
                .filter(r -> r.getPrincipalGuest() != null &&
                        r.getPrincipalGuest().getId() != null &&
                        r.getPrincipalGuest().getId().equals(guest.getId()))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    @Override
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = DataPersistence.loadFromFile(RESERVATIONS_FILE);
        return reservations != null ? new ArrayList<>(reservations) : new ArrayList<>();
    }

    @Override
    public Reservation findReservationById(int reservationId) throws ReservationNotFoundException {
        List<Reservation> reservations = DataPersistence.loadFromFile(RESERVATIONS_FILE);
        if (reservations == null) {
            throw new ReservationNotFoundException("Reserva não encontrada com ID: " + reservationId);
        }

        return reservations.stream()
                .filter(r -> r.getReservationId() != null && r.getReservationId() == reservationId)
                .findFirst()
                .orElseThrow(() -> new ReservationNotFoundException("Reserva não encontrada com ID: " + reservationId));
    }

    @Override
    public void removeReservation(int id) throws ReservationNotFoundException {
        List<Reservation> reservations = DataPersistence.loadFromFile(RESERVATIONS_FILE);
        if (reservations == null) {
            throw new ReservationNotFoundException("Reserva não encontrada com ID: " + id);
        }

        boolean removed = reservations.removeIf(r -> r.getReservationId() != null && r.getReservationId() == id);
        if (!removed) {
            throw new ReservationNotFoundException("Reserva não encontrada com ID: " + id);
        }
        DataPersistence.saveToFile(reservations, RESERVATIONS_FILE);
    }

    @Override
    public void updateReservation(Reservation reservation) throws RoomNotFoundException, ReservationNotFoundException {
        if (reservation == null || reservation.getReservationId() == null) {
            throw new ReservationNotFoundException("Reserva inválida");
        }

        List<Reservation> reservations = DataPersistence.loadFromFile(RESERVATIONS_FILE);
        if (reservations == null) {
            throw new ReservationNotFoundException("Reserva não encontrada com ID: " + reservation.getReservationId());
        }

        for (int i = 0; i < reservations.size(); i++) {
            Reservation existing = reservations.get(i);
            if (existing.getReservationId() != null &&
                    existing.getReservationId().equals(reservation.getReservationId())) {
                reservations.set(i, reservation);
                DataPersistence.saveToFile(reservations, RESERVATIONS_FILE);
                return;
            }
        }
        throw new ReservationNotFoundException("Reserva não encontrada com ID: " + reservation.getReservationId());
    }
}