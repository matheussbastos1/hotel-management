package Models;

public class IndividualRoomModel extends RoomModel {
    private String bedType;
    private static int maxOccupancy = 1;

    public IndividualRoomModel(int roomNumber, String roomType, double price, String bedType, String status, int maxOccupancy) {
        super(roomNumber, roomType, price, status);
        this.bedType = bedType;
        setPrice(200);
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

    public void setMaxOccupancy(int maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
    }

}
