package models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

//Essa classe representa um pagamento realizado por um hóspede para uma fatura específica.

@Data
@NoArgsConstructor

public class Payment {
    private int paymentId;
    private Invoice invoice;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;

}