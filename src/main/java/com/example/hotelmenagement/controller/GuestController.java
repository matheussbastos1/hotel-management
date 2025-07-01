package com.example.hotelmenagement.controller;

import com.example.hotelmenagement.models.Guest;
import com.example.hotelmenagement.repository.impl.GuestRepositoryImpl;
import com.example.hotelmenagement.service.repositoryExceptions.GuestNotFoundException;
import com.example.hotelmenagement.service.repositoryExceptions.RoomNotFoundException;
import java.util.List;
import java.util.Optional;

public class GuestController extends AbstractController<Guest> {
    private final GuestRepositoryImpl guestRepository;

    public GuestController(GuestRepositoryImpl guestRepository) {
        this.guestRepository = guestRepository;
    }

    @Override
    public boolean add(Guest guest) {
        guestRepository.addGuest(guest);
        return true;
    }

    @Override
    public boolean update(Guest guest) throws GuestNotFoundException, RoomNotFoundException {
        guestRepository.updateGuest(guest);
        return true;
    }

    @Override
    public boolean remove(int id) throws GuestNotFoundException {
        Guest guest = guestRepository.findGuestById(id);
        guestRepository.removeGuest(guest);
        return true;
    }

    @Override
    public List<Guest> findAll() throws GuestNotFoundException {
        List<Guest> guests = guestRepository.getGuests();
        if (guests.isEmpty()) {
            throw new GuestNotFoundException("Nenhum hóspede encontrado.");
        }
        return guests;
    }

    @Override
    public Optional<Guest> findById(int id) throws GuestNotFoundException {
        Guest guest = guestRepository.findGuestById(id);
        return Optional.of(guest);
    }
}
