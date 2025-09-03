package com.herts.model;

import lombok.Data;

@Data
public class HandlingUnit extends InputOutputParams {
    private double height;
    private double length;
    private double weight;
    private String stackable;
    private String type;
    private int units;
    private double width;
    private String size;
}
