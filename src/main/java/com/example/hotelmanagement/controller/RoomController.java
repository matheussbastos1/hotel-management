package com.example.hotelmanagement.controller;

import com.example.hotelmanagement.models.Room;
import com.example.hotelmanagement.repository.impl.RoomRepositoryImpl;
import com.example.hotelmanagement.repository.repositoryExceptions.RoomNotFoundException;

import java.util.List;
import java.util.Optional;

public class RoomController extends AbstractController<Room> {
    private final RoomRepositoryImpl roomRepository;

    public RoomController(RoomRepositoryImpl roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public boolean add(Room room) {
        roomRepository.addRoom(room);
        return true;
    }

    @Override
    public boolean update(Room room) throws RoomNotFoundException {
        roomRepository.updateRoom(room);
        return true;
    }

    @Override
    public boolean remove(int id) throws RoomNotFoundException {
        Room room = roomRepository.findRoomByNumber(id);
        roomRepository.removeRoom(room);
        return true;
    }

    @Override
    public List<Room> findAll() throws RoomNotFoundException {
        List<Room> rooms = roomRepository.getAllRooms();
        if (rooms.isEmpty()) {
            throw new RoomNotFoundException("Nenhum quarto encontrado.");
        }
        return rooms;
    }

    @Override
    public Optional<Room> findById(int id) throws RoomNotFoundException {
        Room room = roomRepository.findRoomByNumber(id);
        return Optional.of(room);
    }

    // Método para obter apenas quartos disponíveis
    public List<Room> getAvailableRooms() {
        return roomRepository.getAvailableRooms();
    }
}