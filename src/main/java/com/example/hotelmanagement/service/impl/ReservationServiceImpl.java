package com.example.hotelmanagement.service.impl;

import com.example.hotelmanagement.dto.ReservationDetailsDTO;
import com.example.hotelmanagement.models.*;
import com.example.hotelmanagement.repository.InvoiceRepository;
import com.example.hotelmanagement.repository.ReservationRepository;
import com.example.hotelmanagement.repository.RoomRepository;
import com.example.hotelmanagement.repository.repositoryExceptions.ReservationNotFoundException;
import com.example.hotelmanagement.repository.repositoryExceptions.RoomNotFoundException;
import com.example.hotelmanagement.service.InvoiceService;
import com.example.hotelmanagement.service.ReservationService;
import com.example.hotelmanagement.service.serviceExceptions.InvalidOperationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceService invoiceService;


    public ReservationServiceImpl(ReservationRepository reservationRepository, RoomRepository roomRepository,
                                  InvoiceRepository invoiceRepository, InvoiceService invoiceService) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.invoiceRepository = invoiceRepository;
        this.invoiceService = invoiceService;
    }

    @Override
    public boolean isRoomAvailable(Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        // Validações básicas
        if (roomId == null || checkInDate == null || checkOutDate == null) {
            return false;
        }

        if (checkInDate.isAfter(checkOutDate) || checkInDate.isBefore(LocalDate.now())) {
            return false;
        }

        try {
            // Verificar se o quarto existe
            roomRepository.findRoomByNumber(roomId.intValue());

            // Buscar todas as reservas e verificar manualmente
            List<Reservation> allReservations = reservationRepository.getAllReservations();

            // Filtrar reservas do quarto específico que não estão canceladas
            List<Reservation> roomReservations = allReservations.stream()
                    .filter(r -> r.getRoom().getRoomNumber() == roomId.intValue() &&
                            r.getStatus() != ReservationStatus.CANCELLED)
                    .collect(Collectors.toList());

            // Verificar se alguma reserva se sobrepõe ao período solicitado
            for (Reservation existingReservation : roomReservations) {
                if (hasDateOverlap(checkInDate, checkOutDate,
                        existingReservation.getCheckInDate(),
                        existingReservation.getCheckOutDate())) {
                    return false; // Existe sobreposição, quarto não está disponível
                }
            }

            return true; // Nenhuma sobreposição encontrada, quarto está disponível
        } catch (RoomNotFoundException e) {
            return false; // Quarto não existe
        }
    }

    @Override
    public Reservation confirmReservation(Long reservationId) throws ReservationNotFoundException {
        Reservation reservation = getReservationById(reservationId);

        reservation.setStatus(ReservationStatus.BOOKED);
        reservation.setUpdatedAt(LocalDateTime.now());

        try {
            reservationRepository.updateReservation(reservation);
            return reservation;
        } catch (RoomNotFoundException e) {
            throw new RuntimeException("Erro ao atualizar reserva: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean canCancelReservation(Long reservationId) throws ReservationNotFoundException {
        Reservation reservation = getReservationById(reservationId);

        // Não pode cancelar se já teve check-in ou já está cancelada
        return reservation.getStatus() != ReservationStatus.CHECKED_IN &&
                reservation.getStatus() != ReservationStatus.CANCELLED &&
                LocalDate.now().isBefore(reservation.getCheckInDate());
    }

    @Override
    public Reservation cancelReservation(Long reservationId, String cancellationReason)
            throws ReservationNotFoundException, InvalidOperationException {

        if (!canCancelReservation(reservationId)) {
            throw new InvalidOperationException("Não é possível cancelar esta reserva. Check-in já realizado ou já cancelada.");
        }

        Reservation reservation = getReservationById(reservationId);
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setUpdatedAt(LocalDateTime.now());

        try {
            reservationRepository.updateReservation(reservation);
            return reservation;
        } catch (RoomNotFoundException e) {
            throw new RuntimeException("Erro ao atualizar reserva: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Reservation> findOverlappingReservations(LocalDate checkInDate, LocalDate checkOutDate) {
        // Buscar todas as reservas
        List<Reservation> allReservations = reservationRepository.getAllReservations();

        // Filtrar apenas reservas não canceladas
        List<Reservation> activeReservations = allReservations.stream()
                .filter(r -> r.getStatus() != ReservationStatus.CANCELLED)
                .collect(Collectors.toList());

        return activeReservations.stream()
                .filter(reservation -> hasDateOverlap(
                        checkInDate, checkOutDate,
                        reservation.getCheckInDate(), reservation.getCheckOutDate()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> findAvailableRoomIds(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkInDate == null || checkOutDate == null || checkInDate.isAfter(checkOutDate)) {
            return new ArrayList<>();
        }

        List<Room> allRooms = roomRepository.getAllRooms();
        return allRooms.stream()
                .filter(room -> isRoomAvailable((long) room.getRoomNumber(), checkInDate, checkOutDate))
                .map(room -> (long) room.getRoomNumber())
                .collect(Collectors.toList());
    }

    @Override
    public boolean validateReservation(Reservation reservation) {
        // Validações básicas
        if (reservation == null || reservation.getGuest() == null ||
                reservation.getRoom() == null || reservation.getCheckInDate() == null ||
                reservation.getCheckOutDate() == null) {
            return false;
        }

        // Verificar se as datas são válidas
        if (reservation.getCheckInDate().isAfter(reservation.getCheckOutDate()) ||
                reservation.getCheckInDate().isBefore(LocalDate.now())) {
            return false;
        }

        // Verificar disponibilidade do quarto
        return isRoomAvailable(
                (long) reservation.getRoom().getRoomNumber(),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate());
    }

    @Override
    public boolean hasOverlap(Reservation r1, Reservation r2) {
        if (r1 == null || r2 == null) {
            return false;
        }

        // Se são para quartos diferentes, não há sobreposição
        if (r1.getRoom().getRoomNumber() != r2.getRoom().getRoomNumber()) {
            return false;
        }

        return hasDateOverlap(
                r1.getCheckInDate(), r1.getCheckOutDate(),
                r2.getCheckInDate(), r2.getCheckOutDate());
    }

    @Override
    public Reservation changeDates(Long reservationId, LocalDate newCheckInDate, LocalDate newCheckOutDate)
            throws ReservationNotFoundException, InvalidOperationException {

        Reservation reservation = getReservationById(reservationId);

        // Verificar se a reserva pode ser alterada
        if (reservation.getStatus() == ReservationStatus.CANCELLED ||
                reservation.getStatus() == ReservationStatus.CHECKED_IN) {
            throw new InvalidOperationException("Não é possível alterar datas de uma reserva cancelada ou já iniciada");
        }

        // Validar novas datas
        if (newCheckInDate == null || newCheckOutDate == null ||
                newCheckInDate.isAfter(newCheckOutDate) ||
                newCheckInDate.isBefore(LocalDate.now())) {
            throw new InvalidOperationException("Datas inválidas para alteração");
        }

        // Criar uma cópia temporária da reserva para verificar disponibilidade
        Reservation tempReservation = new Reservation();
        tempReservation.setRoom(reservation.getRoom());
        tempReservation.setCheckInDate(newCheckInDate);
        tempReservation.setCheckOutDate(newCheckOutDate);

        // Buscar todas as reservas
        List<Reservation> allReservations = reservationRepository.getAllReservations();

        // Filtrar outras reservas para este quarto que não estão canceladas (excluindo a atual)
        List<Reservation> otherReservations = allReservations.stream()
                .filter(r -> r.getRoom().getRoomNumber() == reservation.getRoom().getRoomNumber() &&
                        r.getStatus() != ReservationStatus.CANCELLED &&
                        !r.getReservationId().equals(reservationId))
                .collect(Collectors.toList());

        // Verificar sobreposição com outras reservas
        for (Reservation other : otherReservations) {
            if (hasOverlap(tempReservation, other)) {
                throw new InvalidOperationException("O quarto não está disponível no novo período solicitado");
            }
        }

        // Atualizar a reserva
        reservation.setCheckInDate(newCheckInDate);
        reservation.setCheckOutDate(newCheckOutDate);
        reservation.setUpdatedAt(LocalDateTime.now());

        try {
            reservationRepository.updateReservation(reservation);
            return reservation;
        } catch (RoomNotFoundException e) {
            throw new RuntimeException("Erro ao atualizar reserva: " + e.getMessage(), e);
        }
    }

    @Override
    public Reservation changeRoom(Long reservationId, Long newRoomId)
            throws ReservationNotFoundException, InvalidOperationException {

        Reservation reservation = getReservationById(reservationId);

        // Verificar se a reserva pode ser alterada
        if (reservation.getStatus() == ReservationStatus.CANCELLED ||
                reservation.getStatus() == ReservationStatus.CHECKED_IN) {
            throw new InvalidOperationException("Não é possível alterar o quarto de uma reserva cancelada ou já iniciada");
        }

        try {
            // Verificar se o novo quarto existe
            Room newRoom = roomRepository.findRoomByNumber(newRoomId.intValue());

            // Verificar disponibilidade do novo quarto
            if (!isRoomAvailable(newRoomId, reservation.getCheckInDate(), reservation.getCheckOutDate())) {
                throw new InvalidOperationException("O novo quarto não está disponível no período da reserva");
            }

            // Atualizar a reserva
            reservation.setRoom(newRoom);
            reservation.setUpdatedAt(LocalDateTime.now());

            reservationRepository.updateReservation(reservation);
            return reservation;
        } catch (RoomNotFoundException e) {
            throw new InvalidOperationException("Quarto não encontrado: " + e.getMessage());
        }
    }

    // Método auxiliar para verificar sobreposição de datas
    private boolean hasDateOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return !start1.isAfter(end2) && !end1.isBefore(start2);
    }

    // Método auxiliar para buscar reserva por ID
    private Reservation getReservationById(Long reservationId) throws ReservationNotFoundException {
        return reservationRepository.findReservationById(reservationId.intValue());
    }

    @Override
    public Reservation performCheckIn(Long reservationId) throws ReservationNotFoundException, InvalidOperationException {
        // Verifica se pode fazer check-in usando o método canCheckIn
        if (!canCheckIn(reservationId)) {
            throw new InvalidOperationException("Check-in só pode ser realizado na data de entrada ou até um dia após.");
        }

        // Busca a reserva apenas uma vez
        Reservation reservation = getReservationById(reservationId);

        // Atualiza o status da reserva e do quarto
        reservation.setStatus(ReservationStatus.CHECKED_IN);
        reservation.setUpdatedAt(LocalDateTime.now());

        Room room = reservation.getRoom();
        room.setStatus(RoomStatus.OCCUPIED);

        try {
            // Salva as alterações
            reservationRepository.updateReservation(reservation);
            roomRepository.updateRoom(room);
        } catch (RoomNotFoundException e) {
            throw new RuntimeException("Erro ao atualizar o status do quarto: " + e.getMessage(), e);
        }

        return reservation;
    }

    @Override
    public Reservation performCheckOut(Long reservationId) throws ReservationNotFoundException, InvalidOperationException {
        // Verifica se pode fazer check-out usando o método canCheckOut
        if (!canCheckOut(reservationId)) {
            throw new InvalidOperationException("Check-out só pode ser realizado após o check-in e até a data de saída.");
        }

        // Busca a reserva apenas uma vez
        Reservation reservation = getReservationById(reservationId);

        // Atualiza o status da reserva e do quarto
        reservation.setStatus(ReservationStatus.CHECKED_OUT);
        reservation.setUpdatedAt(LocalDateTime.now());

        Room room = reservation.getRoom();
        room.setStatus(RoomStatus.AVAILABLE);

        try {
            // Salva as alterações
            reservationRepository.updateReservation(reservation);
            roomRepository.updateRoom(room);
        } catch (RoomNotFoundException e) {
            throw new RuntimeException("Erro ao atualizar o status do quarto: " + e.getMessage(), e);
        }

        return reservation;
    }

    @Override
    public boolean canCheckIn(Long reservationId) throws ReservationNotFoundException {
        Reservation reservation = getReservationById(reservationId);

        // Verificar se a reserva está com status BOOKED (não pode estar cancelada ou já iniciada)
        if (reservation.getStatus() != ReservationStatus.BOOKED) {
            return false;
        }

        // Verificar se a data atual é a data de check-in ou um dia após
        LocalDate today = LocalDate.now();
        return today.equals(reservation.getCheckInDate()) ||
                today.equals(reservation.getCheckInDate().plusDays(1));
    }

    @Override
    public boolean canCheckOut(Long reservationId) throws ReservationNotFoundException {
        Reservation reservation = getReservationById(reservationId);

        // O check-out só pode ser realizado se a reserva estiver com status CHECKED_IN
        if (reservation.getStatus() != ReservationStatus.CHECKED_IN) {
            return false;
        }

        // Verificar se a data atual está dentro do período da reserva (até a data de saída)
        LocalDate today = LocalDate.now();
        return !today.isBefore(reservation.getCheckInDate()) &&
                !today.isAfter(reservation.getCheckOutDate());
    }

    @Override
    public List<Reservation> searchReservations(LocalDate startDate, LocalDate endDate,
                                                RoomType roomType, ReservationStatus status,
                                                String guestName) {
        // Buscar todas as reservas
        List<Reservation> allReservations = reservationRepository.getAllReservations();

        // Aplicar filtros de forma encadeada
        return allReservations.stream()
                // Filtrar por período (se as datas forem fornecidas)
                .filter(reservation -> startDate == null ||
                        !reservation.getCheckInDate().isBefore(startDate))
                .filter(reservation -> endDate == null ||
                        !reservation.getCheckOutDate().isAfter(endDate))

                // Filtrar por tipo de quarto
                .filter(reservation -> roomType == null ||
                        reservation.getRoom().getRoomType() == roomType)

                // Filtrar por status da reserva
                .filter(reservation -> status == null ||
                        reservation.getStatus() == status)

                // Filtrar por nome do hóspede (busca parcial, case insensitive)
                .filter(reservation -> guestName == null || guestName.isEmpty() ||
                        reservation.getGuest().getName().toLowerCase()
                                .contains(guestName.toLowerCase()))

                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationDetailsDTO> searchReservationsDetailed(
            LocalDate startDate, LocalDate endDate,
            RoomType roomType, ReservationStatus status,
            String guestName) {

        // Primeiro, obter as reservas filtradas usando o método já existente
        List<Reservation> filteredReservations = searchReservations(
                startDate, endDate, roomType, status, guestName);

        // Converter as reservas em DTOs detalhados
        return filteredReservations.stream()
                .map(this::convertToDetailedDTO)
                .collect(Collectors.toList());
    }

    private ReservationDetailsDTO convertToDetailedDTO(Reservation reservation) {
        ReservationDetailsDTO dto = new ReservationDetailsDTO();

        // Dados da reserva
        dto.setReservationId(reservation.getReservationId());
        dto.setCheckInDate(reservation.getCheckInDate());
        dto.setCheckOutDate(reservation.getCheckOutDate());
        dto.setReservationStatus(reservation.getStatus());
        dto.setCreatedAt(reservation.getCreatedAt());
        dto.setUpdatedAt(reservation.getUpdatedAt());

        // Dados do quarto
        Room room = reservation.getRoom();
        if (room != null) {
            dto.setRoomNumber(room.getRoomNumber());
            dto.setRoomType(room.getRoomType());
            dto.setBedType(room.getBedType());
            dto.setRoomStatus(room.getStatus());
            dto.setRoomPrice(room.getPrice());
        }

        // Dados do hóspede
        Guest guest = reservation.getGuest();
        if (guest != null) {
            dto.setGuestId(guest.getId());
            dto.setGuestName(guest.getName());
            dto.setGuestEmail(guest.getEmail());
            dto.setGuestPhone(guest.getPhone());
        }

        // Dados de pagamento - buscar faturas relacionadas à reserva
        List<Invoice> invoices = invoiceService.findInvoicesForReservation(reservation);
        if (!invoices.isEmpty()) {
            // Considerar a primeira fatura (pode haver lógica mais complexa se necessário)
            Invoice invoice = invoices.get(0);
            dto.setPaid(invoice.isPaid());
            dto.setTotalAmount(invoice.getAmountSpent());

            // Encontrar o último pagamento e seu status
            if (invoice.getPayments() != null && !invoice.getPayments().isEmpty()) {
                Payment lastPayment = invoice.getPayments().stream()
                        .max(Comparator.comparing(Payment::getPaymentDate))
                        .orElse(null);

                if (lastPayment != null) {
                    dto.setPaymentStatus(lastPayment.getPaymentStatus());
                    dto.setPaymentMethod(lastPayment.getPaymentMethod().toString());
                    dto.setLastPaymentDate(lastPayment.getPaymentDate());
                }
            }
        }

        return dto;
    }


}

