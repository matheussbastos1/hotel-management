package Models;

import java.util.ArrayList;

public class RoomRepository {
    private ArrayList<Room> rooms = new ArrayList<>();

    public void addRoom(Room room) {
        rooms.add(room);
    }
    public ArrayList<Room> getAllRooms() {
        return new ArrayList<>(rooms);
    }
    public Room findRoomByNumber(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }
}
