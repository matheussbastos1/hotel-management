package repository;

import service.repositoryExceptions.RoomNotFoundException;
import models.Room;
import models.RoomStatus;
import models.RoomType;

import java.util.List;

public interface RoomRepository {

    void addRoom(Room room);

    Room findRoomByNumber(int roomNumber) throws RoomNotFoundException;

    void updateRoom(Room room) throws RoomNotFoundException;

    List<Room> getAllRooms();

    List<Room> getAvailableRooms();

    List<Room> getRoomsByType(RoomType roomType);

    List<Room> getRoomsByStatus(RoomStatus roomStatus);
}
