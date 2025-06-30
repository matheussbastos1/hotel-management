package repository;

import service.repositoryExceptions.RoomNotFoundException;
import service.repositoryExceptions.GuestNotFoundException;
import models.Guest;

import java.util.List;

public interface GuestRepository {

    void addGuest(Guest guests);

    List<Guest> getGuests();

    Guest findGuestByEmail(String email) throws GuestNotFoundException;

    void removeGuest(Guest guest);

    void updateGuest(Guest guest) throws GuestNotFoundException, RoomNotFoundException;

}
