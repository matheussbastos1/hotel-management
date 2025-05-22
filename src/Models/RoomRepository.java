package Models;

import java.util.ArrayList;

public class RoomRepository {
    private ArrayList<RoomModel> rooms = new ArrayList<>();

    public void addRoom(RoomModel room) {
        rooms.add(room);
    }
    public ArrayList<RoomModel> getAllRooms() {
        return new ArrayList<>(rooms);
    }
    public RoomModel findRoomByNumber(int roomNumber) {
        for (RoomModel room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }
}
