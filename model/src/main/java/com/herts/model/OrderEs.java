package com.herts.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

@Data
public class OrderEs extends InputOutputParams{
    private String lastUpdated;
    private String trackingId;
    private String shipmentId;
    private String shipmentDate;
    private String prevStatus;
    private String customerId;
    private String productType;
    private String status;
    private Date CreateDate;
    private Date OrderedDate;
    private Date PickupDate;
    private Date DeliveryDate;
    private String weight;
    private String height;
    private String offerId;
    private String vendorName;
    private String vendorId;
    private double OfferCost;
}
