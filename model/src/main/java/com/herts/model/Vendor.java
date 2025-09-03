package com.herts.model;

import lombok.Data;

@Data
public class Vendor extends InputOutputParams {
    private String vendorId;
    private String vendorName;
    private String phoneNumber;
    private String emailId;
    private String LatestPickupTime;
    private double distCost;
    private double minCost;
    private double mindist;
}
