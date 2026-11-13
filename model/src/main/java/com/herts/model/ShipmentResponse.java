package com.herts.model;

import lombok.Data;
import java.util.ArrayList;

@Data
public class ShipmentResponse extends InputOutputParams  {
    private int packageCount;
    private String trackingNumber;
    private String pickupNo;
    private String carrierName;
    private String carrierLogo;
    private String estimatedDelivery;
    private String carrierContact;
    private String status;
    private ArrayList<String> progress;
    private Address from;
    private Address to;
    private double weight;
    private double cost;
    private String offerId;
    private String customerName;
    private String shipmentId;
    private String customerId;
}
