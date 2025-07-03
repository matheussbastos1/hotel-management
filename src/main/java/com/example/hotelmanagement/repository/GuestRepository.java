package com.example.hotelmanagement.repository;

import com.example.hotelmanagement.models.Guest;
import com.example.hotelmanagement.repository.repositoryExceptions.GuestNotFoundException;
import com.example.hotelmanagement.repository.repositoryExceptions.RoomNotFoundException;


import java.util.List;



public interface GuestRepository {

    void addGuest(Guest guests);

    List<Guest> getGuests();

    Guest findGuestById(int id) throws GuestNotFoundException;

    void removeGuest(Guest guest);

    void updateGuest(Guest guest) throws GuestNotFoundException, RoomNotFoundException;

}
