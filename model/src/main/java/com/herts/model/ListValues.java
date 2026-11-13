package com.herts.model;

import lombok.Data;
import java.util.List;

@Data
public class ListValues extends InputOutputParams {
    private List<Vendor> vendorList;
    private List<Offer> offerList;
    private List<ShipmentES> shipmentESList;
}
