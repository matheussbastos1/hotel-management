package Models;

public class IndividualRoom extends Room {
    private boolean hasAConsole;

    public IndividualRoom(int roomNumber, String roomType, double price, String bedType, Status status, int maxOccupancy) {
        super(roomNumber, roomType, price, status, maxOccupancy, bedType);
        setBedType("Queen Size");
        setPrice(200);
    }

    public boolean getHasAConsole() {
        return hasAConsole;
    }

    public void setHasAConsole(boolean hasAConsole) {
        this.hasAConsole = hasAConsole;
    }
}
