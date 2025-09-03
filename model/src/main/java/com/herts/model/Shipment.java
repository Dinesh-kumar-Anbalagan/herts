package com.herts.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

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
}
