package com.example.Hotel_management.repository;

import com.example.Hotel_management.repository.repositoryExceptions.RoomNotFoundException;
import com.example.Hotel_management.models.Room;
import com.example.Hotel_management.models.RoomStatus;
import com.example.Hotel_management.models.RoomType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository {

    void addRoom(Room room);

    Room findRoomByNumber(int roomNumber) throws RoomNotFoundException;

    void updateRoom(Room room) throws RoomNotFoundException;

    List<Room> getAllRooms();

    List<Room> getAvailableRooms();

    List<Room> getRoomsByType(RoomType roomType);

    List<Room> getRoomsByStatus(RoomStatus roomStatus);
}
