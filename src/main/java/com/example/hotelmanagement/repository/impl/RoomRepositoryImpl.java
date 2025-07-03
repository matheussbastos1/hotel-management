package com.example.hotelmanagement.repository.impl;

import com.example.hotelmanagement.models.Room;
import com.example.hotelmanagement.models.RoomStatus;
import com.example.hotelmanagement.models.RoomType;
import com.example.hotelmanagement.repository.RoomRepository;
import com.example.hotelmanagement.repository.repositoryExceptions.RoomNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class RoomRepositoryImpl implements RoomRepository {

    private List<Room> rooms = new ArrayList<>();

    @Override
    public void addRoom(Room room) {
        this.rooms.add(room);
    }

    @Override
    public void removeRoom(Room room) throws RoomNotFoundException {
        if (!rooms.remove(room)) {
            throw new RoomNotFoundException("Quarto com o número " + room.getRoomNumber() + " não encontrado para remoção.");
        }
    }

    @Override
    public Room findRoomByNumber(int roomNumber) throws RoomNotFoundException {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }

        throw new RoomNotFoundException("Quarto com o número " + roomNumber + " não encontrado.");
    }


    @Override
    public void updateRoom(Room room) throws RoomNotFoundException {
            for (int i = 0; i < rooms.size(); i++) {
                if (rooms.get(i).getRoomNumber() == room.getRoomNumber()) {
                    rooms.set(i, room);
                    return;
                }
            }
            throw new RoomNotFoundException("Quarto com o número " + room.getRoomNumber() + " não encontrado para atualização.");
    }

    @Override
    public List<Room> getAllRooms() {
        return new ArrayList<>(this.rooms);
    }

    @Override
    public List<Room> getAvailableRooms() {
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (room.getStatus() == RoomStatus.AVAILABLE) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    @Override
    public List<Room> getRoomsByType(RoomType roomType) {
        List<Room> roomsByType = new ArrayList<>();
        for (Room room : rooms) {
            if (room.getRoomType() == roomType) {
                roomsByType.add(room);
            }
        }
        return roomsByType;
    }

    @Override
    public List<Room> getRoomsByStatus(RoomStatus roomStatus) {
        List<Room> roomsByStatus = new ArrayList<>();
        for (Room room : rooms) {
            if (room.getStatus() == roomStatus) {
                roomsByStatus.add(room);
            }
        }
        return roomsByStatus;
    }
}
