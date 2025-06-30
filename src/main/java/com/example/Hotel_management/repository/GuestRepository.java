package com.example.Hotel_management.repository;

import com.example.Hotel_management.repository.repositoryExceptions.RoomNotFoundException;
import com.example.Hotel_management.repository.repositoryExceptions.GuestNotFoundException;
import com.example.Hotel_management.models.Guest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuestRepository {

    void addGuest(Guest guests);

    List<Guest> getGuests();

    Guest findGuestByEmail(String email) throws GuestNotFoundException;

    void removeGuest(Guest guest);

    void updateGuest(Guest guest) throws GuestNotFoundException, RoomNotFoundException;

}
