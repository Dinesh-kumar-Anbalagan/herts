package com.herts.model;

import lombok.Data;

@Data
public class ShipmentES extends InputOutputParams {
    private String shipmentId;
    private String senderCompany;
    private String receiverCompany;
    private String carrier;
    private String shipDate;
    private String tracking;
    private String estimatedDelivery;
    private String customerId;
    private String senderPostalCode;
    private String receiverPostalCode;
    private String pickupNumber;
    private String status;
}
