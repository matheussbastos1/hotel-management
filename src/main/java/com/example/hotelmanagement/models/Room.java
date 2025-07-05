package com.example.hotelmanagement.models;

import lombok.AllArgsConstructor; // Importar
import lombok.Data;
import lombok.NoArgsConstructor;

//Essa classe representa um quarto em um sistema de reservas de hotel.

@Data
@NoArgsConstructor
@AllArgsConstructor // Adicionado para gerar o construtor com todos os parâmetros
public class Room {
    private int roomNumber;
    private RoomType roomType;
    private double price;
    private RoomStatus status;
    private int maxOccupancy;
    private String bedType;
}