package com.herts.dbService.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.herts.model.Context;
import com.herts.model.Customer;
import com.herts.model.InputOutputParams;
import com.herts.orchestrator.service.AbstractService;
import lombok.Data;

import java.util.Map;

@Data
public class CustomerWrapper extends InputOutputParams {
    private Map<String, Object> value;
    private String customerID;

    public Map<String, Object> getValue() {
        return value;
    }

    public void setValue(Map<String, Object> value) {
        this.value = value;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

    public String getEmail() {
        return email;
    }

    private Long id;// JSON string

    public CustomerWrapper(Customer customer) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        this.value = mapper.convertValue(customer, Map.class);
    }

    public CustomerWrapper(Map<String, Object> customer) throws JsonProcessingException {
        this.value = customer;
    }
}