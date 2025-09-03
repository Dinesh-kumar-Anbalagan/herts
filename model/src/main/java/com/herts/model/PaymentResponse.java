package com.herts.model;

import lombok.Data;

@Data
public class PaymentResponse extends InputOutputParams{
    private String clientSecret;

}
