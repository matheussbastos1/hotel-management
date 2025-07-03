package com.example.hotelmanagement.dto;

import com.example.hotelmanagement.models.PaymentStatus;
import com.example.hotelmanagement.models.ReservationStatus;
import com.example.hotelmanagement.models.RoomStatus;
import com.example.hotelmanagement.models.RoomType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ReservationDetailsDTO {
    // Informações da reserva
    private Long reservationId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private ReservationStatus reservationStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Informações do quarto
    private int roomNumber;
    private RoomType roomType;
    private String bedType;
    private RoomStatus roomStatus;
    private double roomPrice;

    // Informações do hóspede
    private Long guestId;
    private String guestName;
    private String guestEmail;
    private String guestPhone;

    // Informações de pagamento
    private boolean isPaid;
    private BigDecimal totalAmount;
    private PaymentStatus paymentStatus;
    private String paymentMethod;
    private LocalDateTime lastPaymentDate;
}