package com.herts.model;

import lombok.Data;

@Data
public class Offer extends InputOutputParams {
    private String customerID;
    private String shipmentId;
    private String offerId;
    private String offerDate;
    private String expriationDate;
    private String vendorId;
    private String vendorName;
    private String vendorEmail;
    private double OfferCost;
    private boolean hasInsurance;
    private String status;
    private String estimateDelivery;
}
