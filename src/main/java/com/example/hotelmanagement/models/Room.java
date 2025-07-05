package com.example.hotelmanagement.models;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
//Essa classe representa um hóspede em um sistema de reservas de hotel.

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    private int roomNumber;
    private RoomType roomType;
    private double price;
    private RoomStatus status;
    private int maxOccupancy;
    private String bedType;

    @Override
    public String toString() {
        return "Quarto " + roomNumber + " (" + bedType + ") - R$" + price;
    }


}
