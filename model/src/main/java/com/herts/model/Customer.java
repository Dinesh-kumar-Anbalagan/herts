package com.herts.model;

import lombok.Data;

@Data
public class Customer extends InputOutputParams{
    private String customerID;
    private String accountNumber;
    private String status;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String password;
    private String email;
    private String role;
    private Address address;
}
