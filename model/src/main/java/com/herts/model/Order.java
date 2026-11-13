package com.herts.model;

import lombok.Data;
import java.util.List;

@Data
public class Order extends InputOutputParams{
    private List<Offer> offerList;
    private Shipment shipment;
    private String status;
}
