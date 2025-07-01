package com.example.hotelmenagement.repository;

import org.springframework.stereotype.Repository;
import com.example.hotelmenagement.models.Guest;
import com.example.hotelmenagement.service.repositoryExceptions.GuestNotFoundException;
import com.example.hotelmenagement.service.repositoryExceptions.RoomNotFoundException;

import java.util.List;

@Repository

public interface GuestRepository {

    void addGuest(Guest guests);

    List<Guest> getGuests();

    Guest findGuestById(int id) throws GuestNotFoundException;

    void removeGuest(Guest guest);

    void updateGuest(Guest guest) throws GuestNotFoundException, RoomNotFoundException;

}
