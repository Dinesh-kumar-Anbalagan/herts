package com.herts.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Data
public abstract class InputOutputParams implements Serializable {
    private static final long serialVersionUID = 1L;
    int apiVersion = 1;
   private String Method ;
   private String message;
   private List<Object> list;
   private Map<String, Object> data = new HashMap<>();
    public InputOutputParams(Map<String, Object> data) {
        this.data = new HashMap<>(data);
    }
    public InputOutputParams(){

    }
    public InputOutputParams(List<Object> list){
        this.list = list;
    }
}
