package service.models;

public enum PaymentStatus {
    APROVED("Approved"),
    COMPLETED("Completed"),
    DECLINED("Declined"),
    PENDING("Pending");

    private final String status;

    PaymentStatus(String status) {
        this.status = status;
    }
}
