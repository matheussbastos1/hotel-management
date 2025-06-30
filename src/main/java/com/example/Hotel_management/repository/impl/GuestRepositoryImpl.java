package com.example.Hotel_management.repository.impl;

import com.example.Hotel_management.repository.GuestRepository;
import com.example.Hotel_management.repository.repositoryExceptions.GuestNotFoundException;
import com.example.Hotel_management.models.Guest;

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
    public Guest findGuestByEmail(String email) throws GuestNotFoundException {
        for (Guest guest : guests) {
            if (guest.getGuestEmail().equals(email)) {
                return guest;
            }
        }// or throw an exception if preferred
        throw new GuestNotFoundException("Hóspede com o email " + email + " não encontrado.");
    }

    @Override
    public void removeGuest(Guest guest) {
        guests.remove(guest);
    }

    @Override
    public void updateGuest(Guest guest) throws GuestNotFoundException {
        for (int i = 0; i < guests.size(); i++) {
            if (guests.get(i).getGuestEmail().equals(guest.getGuestEmail())) {
                guests.set(i, guest);
                return;
            }
        }
        throw new GuestNotFoundException("Hóspede com o email " + guest.getGuestEmail() + " não encontrado para atualização.");
    }
}