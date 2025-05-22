package Models;

public class CoupleRoomModel extends RoomModel {

    private String bedType;
    private static int maxOccupancy = 2;

    public CoupleRoomModel(int roomNumber, String roomType, double price, String bedType, String status, int maxOccupancy) {
        super(roomNumber, roomType, price, status);
        setBedType("King Size");
        setPrice(300);
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
