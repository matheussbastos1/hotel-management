package Models;

public class FamilyRoomModel extends RoomModel {
    private String bedType;
    private static int maxOccupancy = 4;

    public FamilyRoomModel(int roomNumber, String roomType, double price, String bedType, String status, int maxOccupancy) {
        super(roomNumber, roomType, price, status);
        setBedType(bedType);
        setPrice(500);
    }

    public String getBedType() {
        return bedType;
    }

    public void setBedType(String bedType) {
        this.bedType = bedType;
    }

    public int getMaxOccupancy() {
        return maxOccupancy;
    }

}
