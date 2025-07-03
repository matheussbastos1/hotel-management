package com.example.hotelmanagement.repository.repositoryExceptions;

public class GuestNotFoundException extends Exception {

    String guestEmail;

    public GuestNotFoundException(String message) {

        super(message);
        this.guestEmail = guestEmail;
    }
}
