package com.example.hotelmanagement.repository.impl;

import com.example.hotelmanagement.models.Room;
import com.example.hotelmanagement.models.RoomStatus;
import com.example.hotelmanagement.models.RoomType;
import com.example.hotelmanagement.repository.RoomRepository;
import com.example.hotelmanagement.repository.repositoryExceptions.RoomNotFoundException;
import com.example.hotelmanagement.util.DataPersistence;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoomRepositoryImpl implements RoomRepository {

    private static final String FILE_NAME = "rooms";
    private List<Room> rooms;

    public RoomRepositoryImpl() {
        loadData();
    }

    private void loadData() {
        rooms = DataPersistence.loadFromFile(FILE_NAME);
        if (rooms == null) {
            rooms = new ArrayList<>();
        }
    }

    private void saveData() {
        DataPersistence.saveToFile(rooms, FILE_NAME);
    }

    @Override
    public void addRoom(Room room) {
        rooms.add(room);
        saveData();
    }

    @Override
    public Room findRoomByNumber(int roomNumber) throws RoomNotFoundException {
        return rooms.stream()
                .filter(room -> room.getRoomNumber() == roomNumber)
                .findFirst()
                .orElseThrow(() -> new RoomNotFoundException("Quarto com número " + roomNumber + " não encontrado"));
    }

    @Override
    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms);
    }

    @Override
    public List<Room> getAvailableRooms() {
        return rooms.stream()
                .filter(room -> room.getStatus() == RoomStatus.AVAILABLE)
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> getRoomsByType(RoomType roomType) {
        return rooms.stream()
                .filter(room -> room.getRoomType() == roomType)
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> getRoomsByStatus(RoomStatus roomStatus) {
        return rooms.stream()
                .filter(room -> room.getStatus() == roomStatus)
                .collect(Collectors.toList());
    }

    @Override
    public void updateRoom(Room room) throws RoomNotFoundException {
        int index = -1;
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getRoomNumber() == room.getRoomNumber()) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            throw new RoomNotFoundException("Quarto com número " + room.getRoomNumber() + " não encontrado");
        }

        rooms.set(index, room);
        saveData();
    }

    @Override
    public void removeRoom(int roomNumber) throws RoomNotFoundException {
        boolean removed = rooms.removeIf(room -> room.getRoomNumber() == roomNumber);
        if (!removed) {
            throw new RoomNotFoundException("Quarto com número " + roomNumber + " não encontrado");
        }
        saveData();
    }
}