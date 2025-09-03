package com.herts.model;

import lombok.Data;
import java.util.ArrayList;

@Data
public class Address extends InputOutputParams {
   private String address;
   private String country;
   private String locality;
   private String postalCode;
   private String county;
}
