package com.herts.model;

import lombok.Data;
import java.util.Date;

@Data
public class Contract {
    private String contractId;
    private String contractType;
    private Date effectiveDate;
    private Date endDate;
    private String productType;
    private Address billingAddress;
}
