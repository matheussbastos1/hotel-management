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
    public boolean add(Room room) { // AGORA É PUBLIC
        roomRepository.addRoom(room);
        return true;
    }

    @Override
    public boolean update(Room room) throws RoomNotFoundException { // AGORA É PUBLIC
        roomRepository.updateRoom(room);
        return true;
    }

    @Override
    public boolean remove(int id) throws RoomNotFoundException { // AGORA É PUBLIC
        // O findRoomByNumber já existe no repositório e é usado aqui
        Room room = roomRepository.findRoomByNumber(id);
        roomRepository.removeRoom(room);
        return true;
    }

    @Override
    public List<Room> findAll() throws RoomNotFoundException { // JÁ ERA PUBLIC
        List<Room> rooms = roomRepository.getAllRooms();
        if (rooms.isEmpty()) {
            throw new RoomNotFoundException("Nenhum quarto encontrado.");
        }
        return rooms;
    }

    @Override
    public Optional<Room> findById(int id) throws RoomNotFoundException { // JÁ ERA PUBLIC
        // Este método 'findById' é a implementação do 'findByNumber' que o Copilot mencionou.
        // O FXMLController chamará 'findById'.
        Room room = roomRepository.findRoomByNumber(id);
        return Optional.of(room);
    }
}