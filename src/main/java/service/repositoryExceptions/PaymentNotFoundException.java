package service.repositoryExceptions;

public class PaymentNotFoundException extends Exception {

    int paymentId;

    public PaymentNotFoundException(String message) {

        super(message);
        this.paymentId = paymentId;
    }
}
