package com.herts.model;

import lombok.Data;
import java.util.Date;

@Data
public class StatusTracking {
    private String shipmentId;
    private Date DeliveryDate;
    private Date updateDate;
    private String Status;
}
