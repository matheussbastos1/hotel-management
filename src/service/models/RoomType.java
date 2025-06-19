package service.models;

public enum RoomType {
    INDIVIDUAL("Individual", 1),
    COUPLE("Couple", 2),
    FAMILY("Family", 5);

    private final String type;
    private final int maxOccupancy;
    RoomType(String type, int maxOccupancy) {
        this.type = type;
        this.maxOccupancy = maxOccupancy;
    }
}
