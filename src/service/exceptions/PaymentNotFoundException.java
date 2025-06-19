package service.exceptions;

public class PaymentNotFoundException extends Exception {

    int paymentId;

    public PaymentNotFoundException(String message) {

        super(message);
        this.paymentId = paymentId;
    }
}
