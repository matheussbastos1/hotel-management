package Models;

public enum Status {
    AVAILABLE("Available"),
    BOOKED("Booked"),
    MAINTENANCE("Maintenance"),
    OCCUPIED("Occupied");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
