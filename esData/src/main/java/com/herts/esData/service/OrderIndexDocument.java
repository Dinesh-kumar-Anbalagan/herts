package com.herts.esData.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.herts.model.*;
import com.herts.orchestrator.exception.CustomizeException;
import com.herts.orchestrator.service.AbstractService;
import com.herts.orchestrator.service.ServiceBus;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.lang.reflect.Method;

public class OrderIndexDocument implements AbstractService<InputOutputParams, String> {
    @Autowired
    private ElasticsearchClient client;
    @Autowired
    private ServiceBus serviceBus;

    private static final String INDEX_NAME = "Order";

    public Address getProductById(String id) throws IOException {
        return client.get(g -> g
                .index(INDEX_NAME)
                .id(id), Address.class
        ).source();
    }

    @Override
    public String runProcess(Context ctx, InputOutputParams input) {
        String methodName = ctx.getMethod();

        ObjectMapper mapper = new ObjectMapper();
        Order order = mapper.convertValue(input.getData(), Order.class);

        if (methodName.isBlank()) {
            throw new CustomizeException("Method is not Specified");
        }

        try {
            Method method = this.getClass().getMethod(methodName);
            return (String) method.invoke(this, order);

        } catch (Exception ex) {
            throw new CustomizeException("Method not Found");
        }

    }

    public String creation(Order order) {
        Shipment shipment = order.getShipment();
        OrderEs orderEs = new OrderEs();
        orderEs.setShipmentId(shipment.getShipmentId());
        orderEs.setShipmentDate(shipment.getShipmentDate());
        //orderEs.setOrderedDate(.getOrderedDate());
//        orderEs.setHeight(shipment.getHandlingUnit().getHeight());
//        orderEs.setWeight(shipment.getWeight());
        
        try {
            Customer cust = new Customer();
            IndexResponse response = client.index(i -> i
                    .index(INDEX_NAME)
                    .id(String.valueOf( cust.getCustomerID()))
                    .document(cust)
            );

            System.out.println("Status: " + response.result());

            return "Uploaded SuccessFully";
        } catch (Exception e) {
            throw new CustomizeException("Address Data not Properly uploaded in the Elastic Search");
        }
    }

}