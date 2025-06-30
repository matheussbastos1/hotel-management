package service.repositoryExceptions;

public class GuestNotFoundException extends Exception {

    String guestEmail;

    public GuestNotFoundException(String message) {

        super(message);
        this.guestEmail = guestEmail;
    }
}
