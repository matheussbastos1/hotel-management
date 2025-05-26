package Models;

import Models.GuestModel;
import java.util.ArrayList;
import java.util.List;

public class GuestsRepository {
    private List<GuestModel> guests = new ArrayList<>();

    public void addGuest(GuestModel guest) {
        guests.add(guest);
    }

    public List<GuestModel> getAllGuests() {
        return new ArrayList<>(guests);
    }

    public GuestModel findGuestByEmail(String email) {
        for (GuestModel guest : guests) {
            if (guest.getGuestEmail().equals(email)) {
                return guest;
            }
        }
        return null;
    }

    public void removeGuest(GuestModel guest) {
        guests.remove(guest);
    }
}