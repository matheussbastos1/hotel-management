package com.example.hotelmanagement.models;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

//Essa classe representa uma fatura gerada para uma estadia de um hóspede.

@Data
@NoArgsConstructor

public class Invoice {
    private int invoiceId;
    private Stay stay;
    private Map<String, BigDecimal> charges;
    private BigDecimal amountSpent;
    private boolean paid;
    private List<Payment> payments;
}