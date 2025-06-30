package com.example.Hotel_management.models;
import lombok.Data;
import lombok.NoArgsConstructor;

//Essa classe representa um hóspede em um sistema de reservas de hotel.

@Data
@NoArgsConstructor

public class Room {
    private int roomNumber;
    private RoomType roomType;
    private double price;
    private RoomStatus status;
    private int maxOccupancy;
    private String bedType;

}
