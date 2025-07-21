package com.example.hotelmanagement.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StayReportDTO {
    private Long reservationId;
    private String guestName;
    private Integer roomNumber;
    private String checkInDate;
    private String checkOutDate;
    private Long numberOfNights;
    private String status;
}