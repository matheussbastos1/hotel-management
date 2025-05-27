package Models;

import java.util.ArrayList;
import java.util.List;

public class GuestsRepository {
    private List<Guest> guests = new ArrayList<>();

    public void addGuest(Guest guest) {
        guests.add(guest);
    }

    public List<Guest> getAllGuests() {
        return new ArrayList<>(guests);
    }

    public Guest findGuestByEmail(String email) {
        for (Guest guest : guests) {
            if (guest.getGuestEmail().equals(email)) {
                return guest;
            }
        }
        return null;
    }

    public void removeGuest(Guest guest) {
        guests.remove(guest);
    }
}