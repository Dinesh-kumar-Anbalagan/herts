package com.herts.model;

import lombok.Data;

@Data
public class ReportResponse extends InputOutputParams {
    private String filename;
    private String base64;
}
