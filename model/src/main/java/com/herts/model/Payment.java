package com.herts.model;

import lombok.Data;

@Data
public class Payment {
    private String paymentId;
    private String paymentType;
    private String expiration;
    private boolean isActive;
    private boolean isAutoPayEnabled;
    private String CardNumber;
    private String nameOnAccount;
    private Address billingAddress;
}
