package Models;

public class FamilyRoom extends Room {

    private boolean hasCradle;

    public FamilyRoom(int roomNumber, String roomType, double price, String bedType, Status status, int maxOccupancy) {
        super(roomNumber, roomType, price, status, maxOccupancy, bedType);
        setBedType("Queen Size");
        setPrice(500);
        this.hasCradle = this.getMaxOccupancy() > 3;
    }

    public boolean isHasCradle() {
        return hasCradle;
    }

    public void setHasCradle(boolean hasCradle) {
        this.hasCradle = hasCradle;
    }
}
