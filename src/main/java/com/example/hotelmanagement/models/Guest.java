package com.example.hotelmanagement.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Essa classe representa um hóspede em um sistema de reservas de hotel.

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Guest {
    private Long id;
    private String Name;
    private String Email;
    private String Phone;
    private String Address;
    private String Cpf;
}