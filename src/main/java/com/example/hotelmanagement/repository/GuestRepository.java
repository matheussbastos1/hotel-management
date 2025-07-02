package com.example.hotelmanagement.repository;

import org.springframework.stereotype.Repository;
import com.example.hotelmanagement.models.Guest;
import com.example.hotelmanagement.repository.repositoryExceptions.GuestNotFoundException;
import com.example.hotelmanagement.repository.repositoryExceptions.RoomNotFoundException;

import java.util.List;

@Repository

public interface GuestRepository {

    void addGuest(Guest guests);

    List<Guest> getGuests();

    Guest findGuestById(int id) throws GuestNotFoundException;

    void removeGuest(Guest guest);

    void updateGuest(Guest guest) throws GuestNotFoundException, RoomNotFoundException;

}
