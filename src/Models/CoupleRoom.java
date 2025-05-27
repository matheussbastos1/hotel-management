package Models;

public class CoupleRoom extends Room {

   private boolean hasKitchen;

    public CoupleRoom(int roomNumber, String roomType, double price, String bedType, Status status, int maxOccupancy) {
        super(roomNumber, roomType, price, status, maxOccupancy, bedType);
        setBedType("King Size");
        setPrice(300);
    }

    public boolean isHasKitchen() {
        return hasKitchen;
    }

    public void setHasKitchen(boolean hasKitchen) {
        this.hasKitchen = hasKitchen;
    }
}
