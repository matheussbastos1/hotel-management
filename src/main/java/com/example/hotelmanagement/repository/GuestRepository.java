package com.example.hotelmanagement.repository;

import com.example.hotelmanagement.models.Guest;
import com.example.hotelmanagement.repository.repositoryExceptions.GuestNotFoundException;
import com.example.hotelmanagement.repository.repositoryExceptions.RoomNotFoundException;


import java.util.List;
import java.util.Optional;


public interface GuestRepository {

    void addGuest(Guest guests);

    List<Guest> getGuests();

    Guest findGuestById(int id) throws GuestNotFoundException;

    void removeGuest(int id) throws GuestNotFoundException;

    void updateGuest(Guest guest) throws GuestNotFoundException, RoomNotFoundException;

    public Optional<Guest> findGuestByCpf(String cpf) throws GuestNotFoundException;
}
