package com.example.hotelmenagement.repository;

import com.example.hotelmenagement.service.repositoryExceptions.RoomNotFoundException;
import com.example.hotelmenagement.models.Room;
import com.example.hotelmenagement.models.RoomStatus;
import com.example.hotelmenagement.models.RoomType;

import java.util.List;

public interface RoomRepository {

    void addRoom(Room room);

    void removeRoom(Room room) throws RoomNotFoundException;

    Room findRoomByNumber(int roomNumber) throws RoomNotFoundException;

    void updateRoom(Room room) throws RoomNotFoundException;

    List<Room> getAllRooms();

    List<Room> getAvailableRooms();

    List<Room> getRoomsByType(RoomType roomType);

    List<Room> getRoomsByStatus(RoomStatus roomStatus);
}
