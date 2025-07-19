package com.example.hotelmanagement.repository;

import com.example.hotelmanagement.models.Room;
import com.example.hotelmanagement.models.RoomStatus;
import com.example.hotelmanagement.models.RoomType;
import com.example.hotelmanagement.repository.repositoryExceptions.RoomNotFoundException;
import java.time.LocalDate;

import java.util.List;


public interface RoomRepository {

    void addRoom(Room room);

    void removeRoom(Room room) throws RoomNotFoundException;

    // Adicione à interface
    List<Room> findAvailableRoomsByDateRange(LocalDate checkIn, LocalDate checkOut);

    Room findRoomByNumber(int roomNumber) throws RoomNotFoundException;

    void updateRoom(Room room) throws RoomNotFoundException;

    List<Room> getAllRooms();

    List<Room> getAvailableRooms();

    List<Room> getRoomsByType(RoomType roomType);

    List<Room> getRoomsByStatus(RoomStatus roomStatus);
}
