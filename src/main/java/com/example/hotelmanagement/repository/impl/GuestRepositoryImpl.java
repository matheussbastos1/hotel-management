package com.example.hotelmanagement.repository.impl;

import com.example.hotelmanagement.models.Guest;
import com.example.hotelmanagement.repository.GuestRepository;
import com.example.hotelmanagement.repository.repositoryExceptions.GuestNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class GuestRepositoryImpl implements GuestRepository {


    private final List<Guest> guests = new ArrayList<>();

    @Override
    public void addGuest(Guest guests) {
        this.guests.add(guests);
    }

    @Override
    public List<Guest> getGuests() {
        return new ArrayList<>(this.guests);
    }

    @Override
    public Guest findGuestById(int id) throws GuestNotFoundException {
        for (Guest guest : guests) {
            if (guest.getId() == id) {
                return guest;
            }
        }
        throw new GuestNotFoundException("Hóspede com o ID " + id + " não encontrado.");
    }

    @Override
    public void removeGuest(Guest guest) {
        guests.remove(guest);
    }

    @Override
    public void updateGuest(Guest guest) throws GuestNotFoundException {
        for (int i = 0; i < guests.size(); i++) {
            if (guests.get(i).getEmail().equals(guest.getEmail())) {
                guests.set(i, guest);
                return;
            }
        }
        throw new GuestNotFoundException("Hóspede com o email " + guest.getEmail() + " não encontrado para atualização.");
    }
}