package com.herts.model;

import lombok.Data;

@Data
public class Shipment extends InputOutputParams{
    private Address sender;
    private Address receiver;
    private HandlingUnit handlingUnit;
    private String shipmentDate;
    private String shipmentId;
    private String customerId;
    private String trackingId;
    private String pickupNumber;
    private String status;
    private String currentStatus;

}
