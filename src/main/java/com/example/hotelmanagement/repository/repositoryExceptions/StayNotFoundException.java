package com.example.hotelmanagement.repository.repositoryExceptions;

public class StayNotFoundException extends RuntimeException {

   int stayId;

  public StayNotFoundException(String message) {

      super(message);
        this.stayId = stayId;

    }
}
