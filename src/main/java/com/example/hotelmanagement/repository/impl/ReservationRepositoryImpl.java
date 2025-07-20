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
import java.util.stream.Collectors;

public class ReservationRepositoryImpl implements ReservationRepository {

    private static final String FILE_NAME = "reservations";
    private List<Reservation> reservations;

    public ReservationRepositoryImpl() {
        loadData();
    }

    private void loadData() {
        reservations = DataPersistence.loadFromFile(FILE_NAME);
        if (reservations == null) {
            reservations = new ArrayList<>();
        }
    }

    private void saveData() {
        DataPersistence.saveToFile(reservations, FILE_NAME);
    }

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
        // Gera ID se não existir
        if (reservation.getReservationId() == null) {
            long newId = reservations.stream()
                    .mapToLong(Reservation::getReservationId)
                    .max()
                    .orElse(1000L) + 1;
            reservation.setReservationId(newId);
        }
        reservations.add(reservation);
        saveData();
    }

    @Override
    public List<Reservation> getReservationsByGuest(Guest guest) {
        List<Reservation> guestReservations = new ArrayList<>();
        for (Reservation reservation : reservations) {
            if (reservation.getGuest().equals(guest)) {
                guestReservations.add(reservation);
            }
        }
        return guestReservations;
    }

    @Override
    public Reservation findReservationById(int id) throws ReservationNotFoundException {
        return reservations.stream()
                .filter(reservation -> reservation.getReservationId() == id)
                .findFirst()
                .orElseThrow(() -> new ReservationNotFoundException("Reserva com ID " + id + " não encontrada"));
    }

    @Override
    public void removeReservation(int id) throws ReservationNotFoundException {
        boolean removed = reservations.removeIf(reservation -> reservation.getReservationId() == id);
        if (!removed) {
            throw new ReservationNotFoundException("Reserva com ID " + id + " não encontrada");
        }
        saveData();

    }

    @Override
    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations);
    }

    @Override
    public void updateReservation(Reservation reservation) throws RoomNotFoundException {
        int index = -1;
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).getReservationId().equals(reservation.getReservationId())) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            throw new RoomNotFoundException("Reserva com ID " + reservation.getReservationId() + " não encontrada");
        }

        reservations.set(index, reservation);
        saveData();
    }

}