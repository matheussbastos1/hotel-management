package Models;


import java.time.LocalDate;
import java.util.ArrayList;

public class HotelTest {
    public static void main(String[] args) {
        // Testando RoomModel e subclasses
        IndividualRoomModel individualRoom = new IndividualRoomModel(101, "Single", 100.0, "Queen", "Available", 1);
        CoupleRoomModel coupleRoom = new CoupleRoomModel(102, "Double", 150.0, "King", "Available", 2);
        FamilyRoomModel familyRoom = new FamilyRoomModel(103, "Family", 200.0, "Queen", "Available", 4);

        System.out.println("Individual Room: " + individualRoom.getRoomType() + ", Price: " + individualRoom.getPrice());
        System.out.println("Couple Room: " + coupleRoom.getRoomType() + ", Price: " + coupleRoom.getPrice());
        System.out.println("Family Room: " + familyRoom.getRoomType() + ", Price: " + familyRoom.getPrice());

        // Testando GuestModel
        ArrayList<GuestModel> guests = new ArrayList<>();
        GuestModel guest1 = new GuestModel("John Doe", "john@example.com", "123456789", "123 Street", guests, individualRoom);
        guests.add(guest1);

        System.out.println("Guest Name: " + guest1.getGuestName());
        System.out.println("Guest Email: " + guest1.getGuestEmail());
        System.out.println("Assigned Room: " + guest1.getRoom().getRoomType());

        // Testando ReservationModel
        ReservationModel reservation = new ReservationModel(guest1, coupleRoom, LocalDate.of(2023, 10, 1), LocalDate.of(2023, 10, 5), "Confirmed");

        System.out.println("Reservation Guest: " + reservation.getGuest().getGuestName());
        System.out.println("Reservation Room: " + reservation.getRoom().getRoomType());
        System.out.println("Check-in Date: " + reservation.getCheckInDate());
        System.out.println("Check-out Date: " + reservation.getCheckOutDate());
        System.out.println("Reservation Status: " + reservation.getStatus());

        // Testando RoomRepository
        RoomRepository roomRepository = new RoomRepository();
        roomRepository.addRoom(individualRoom);
        roomRepository.addRoom(coupleRoom);
        roomRepository.addRoom(familyRoom);

        System.out.println("All Rooms: " + roomRepository.getAllRooms().size());
        RoomModel foundRoom = roomRepository.findRoomByNumber(102);
        System.out.println("Found Room: " + (foundRoom != null ? foundRoom.getRoomType() : "Not Found"));
    }
}
