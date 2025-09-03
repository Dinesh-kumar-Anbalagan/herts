package com.herts.model;

import lombok.Data;

@Data
public class Report extends InputOutputParams{
    private String shipmentId;
    private String offerId;
    private String customerID;
    private String base64;
}
