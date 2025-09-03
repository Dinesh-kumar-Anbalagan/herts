package com.herts.model;

import lombok.Data;

@Data
public class PaymentRequest extends InputOutputParams {
    private double amount;
    private String currency;
}
