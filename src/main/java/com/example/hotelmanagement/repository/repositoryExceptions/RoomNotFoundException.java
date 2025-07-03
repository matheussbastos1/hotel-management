package com.example.hotelmanagement.repository.repositoryExceptions;

public class RoomNotFoundException extends Exception {
    int roomNumber;

    public RoomNotFoundException(String message) {

        super(message);
        this.roomNumber = roomNumber;
    }
}
