package repository;

import service.exceptions.GuestNotFoundException;
import service.exceptions.RoomNotFoundException;
import service.models.Guest;

import java.util.ArrayList;
import java.util.List;

public interface GuestRepository {

    void addGuest(Guest guests);

    List<Guest> getGuests();

    Guest findGuestByEmail(String email) throws GuestNotFoundException;

    void removeGuest(Guest guest);

    void updateGuest(Guest guest) throws GuestNotFoundException, RoomNotFoundException;

}
