package Models;

public class RoomModel {
    private int roomNumber;
    private String roomType;
    private double price;
    private String status;

    public RoomModel(int roomNumber, String roomType, double price, String status) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.price = price;
        this.status = status;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
