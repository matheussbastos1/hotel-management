package repository;

import service.exceptions.RoomNotFoundException;
import service.models.Room;
import service.models.RoomType;
import service.models.RoomStatus;

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
