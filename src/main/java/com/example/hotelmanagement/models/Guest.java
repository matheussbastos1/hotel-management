package com.example.hotelmanagement.models;

import lombok.Data;
import lombok.NoArgsConstructor;

//Essa classe representa um hóspede em um sistema de reservas de hotel.

@Data
@NoArgsConstructor
public class Guest {
    private Long id;
    private String guestName;
    private String guestEmail;
    private String guestPhone;
    private String guestAddress;
}