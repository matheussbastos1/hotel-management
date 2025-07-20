package com.example.hotelmanagement.repository.impl;

import com.example.hotelmanagement.models.Guest;
import com.example.hotelmanagement.repository.GuestRepository;
import com.example.hotelmanagement.repository.repositoryExceptions.GuestNotFoundException;
import com.example.hotelmanagement.util.DataPersistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GuestRepositoryImpl implements GuestRepository {

    private static final String FILE_NAME = "guests";
    private List<Guest> guests;

    public GuestRepositoryImpl() {
        loadData();
    }

    private void loadData() {
        guests = DataPersistence.loadFromFile(FILE_NAME);
        if (guests == null) {
            guests = new ArrayList<>();
        }
    }

    private void saveData() {
        DataPersistence.saveToFile(guests, FILE_NAME);
    }

    @Override
    public void addGuest(Guest guest) {
        // Gera ID se não existir
        if (guest.getId() == null) {
            long newId = guests.stream()
                    .mapToLong(Guest::getId)
                    .max()
                    .orElse(0L) + 1;
            guest.setId(newId);
        }
        guests.add(guest);
        saveData();
    }

    @Override
    public Guest findGuestById(int id) throws GuestNotFoundException {
        return guests.stream()
                .filter(guest -> guest.getId() == id)
                .findFirst()
                .orElseThrow(() -> new GuestNotFoundException("Hóspede com ID " + id + " não encontrado"));
    }

    @Override
    public List<Guest> getGuests() {
        return new ArrayList<>(guests);
    }

    @Override
    public void updateGuest(Guest guest) throws GuestNotFoundException {
        int index = -1;
        for (int i = 0; i < guests.size(); i++) {
            if (guests.get(i).getId().equals(guest.getId())) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            throw new GuestNotFoundException("Hóspede com ID " + guest.getId() + " não encontrado");
        }

        guests.set(index, guest);
        saveData();
    }

    @Override
    public void removeGuest(int id) throws GuestNotFoundException {
        boolean removed = guests.removeIf(guest -> guest.getId() == id);
        if (!removed) {
            throw new GuestNotFoundException("Hóspede com ID " + id + " não encontrado");
        }
        saveData();
    }

    @Override
    public Optional<Guest> findGuestByCpf(String cpf) {
        return guests.stream()
                .filter(guest -> guest.getCpf().equals(cpf))
                .findFirst();
    }
}