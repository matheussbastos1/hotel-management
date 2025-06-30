package com.example.Hotel_management.service;

import com.example.Hotel_management.dto.ReservationDetailsDTO;
import com.example.Hotel_management.models.Reservation;
import com.example.Hotel_management.models.ReservationStatus;
import com.example.Hotel_management.models.RoomType;
import com.example.Hotel_management.repository.repositoryExceptions.ReservationNotFoundException;
import com.example.Hotel_management.service.serviceExceptions.InvalidOperationException;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {

    /**
     * Verifica se um quarto está disponível para o período solicitado
     */
    boolean isRoomAvailable(Long roomId, LocalDate checkInDate, LocalDate checkOutDate);

    /**
     * Confirma uma reserva pendente
     */
    Reservation confirmReservation(Long reservationId) throws ReservationNotFoundException;

    /**
     * Verifica se uma reserva pode ser cancelada
     */
    boolean canCancelReservation(Long reservationId) throws ReservationNotFoundException;

    /**
     * Cancela uma reserva existente
     */
    Reservation cancelReservation(Long reservationId, String cancellationReason)
            throws ReservationNotFoundException, InvalidOperationException;

    /**
     * Busca reservas que se sobrepõem ao período especificado
     */
    List<Reservation> findOverlappingReservations(LocalDate checkInDate, LocalDate checkOutDate);

    /**
     * Encontra quartos disponíveis para o período especificado
     */
    List<Long> findAvailableRoomIds(LocalDate checkInDate, LocalDate checkOutDate);

    /**
     * Valida uma reserva de acordo com as regras de negócio
     */
    boolean validateReservation(Reservation reservation);

    /**
     * Verifica se há sobreposição entre duas reservas
     */
    boolean hasOverlap(Reservation r1, Reservation r2);

    Reservation changeDates(Long reservationId, LocalDate newCheckInDate, LocalDate newCheckOutDate)
            throws ReservationNotFoundException, InvalidOperationException;

    Reservation changeRoom(Long reservationId, Long newRoomId)
            throws ReservationNotFoundException, InvalidOperationException;

    boolean canCheckIn(Long reservationId) throws ReservationNotFoundException;
    Reservation performCheckIn(Long reservationId) throws ReservationNotFoundException, InvalidOperationException;

    boolean canCheckOut(Long reservationId) throws ReservationNotFoundException;
    Reservation performCheckOut(Long reservationId) throws ReservationNotFoundException, InvalidOperationException;

    List<Reservation> searchReservations(LocalDate startDate, LocalDate endDate,
                                         RoomType roomType, ReservationStatus status,
                                         String guestName);


    List<ReservationDetailsDTO> searchReservationsDetailed(
            LocalDate startDate,
            LocalDate endDate,
            RoomType roomType,
            ReservationStatus status,
            String guestName);

}