package com.example.hotelmanagement.models;

public enum RoomType {
    INDIVIDUAL("Individual", 1, 200),
    COUPLE("Couple", 2, 400),
    FAMILY("Family", 5, 800),;

    private final String type;
    private final int maxOccupancy;
    private final int price;
    RoomType(String type, int maxOccupancy, int price) {
        this.type = type;
        this.maxOccupancy = maxOccupancy;
        this.price = price;
    }
}
