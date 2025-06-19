package service.models;

//Essa classe representa um hóspede em um sistema de reservas de hotel.

public class Guest {
    private Long id;
    private String guestName;
    private String guestEmail;
    private String guestPhone;
    private String guestAddress;


    public Guest(Long id, String guestName, String guestEmail, String guestPhone, String guestAddress) {
        this.id = id;
        this.guestName = guestName;
        this.guestEmail = guestEmail;
        this.guestPhone = guestPhone;
        this.guestAddress = guestAddress;

    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getGuestEmail() {
        return guestEmail;
    }

    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
    }

    public String getGuestPhone() {
        return guestPhone;
    }

    public void setGuestPhone(String guestPhone) {
        this.guestPhone = guestPhone;
    }

    public String getGuestAddress() {
        return guestAddress;
    }

    public void setGuestAddress(String guestAddress) {
        this.guestAddress = guestAddress;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }
}