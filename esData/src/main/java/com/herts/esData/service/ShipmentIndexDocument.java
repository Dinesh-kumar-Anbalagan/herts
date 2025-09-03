package com.herts.esData.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.herts.model.*;
import com.herts.orchestrator.service.AbstractService;
import com.herts.orchestrator.exception.CustomizeException;
import com.herts.orchestrator.service.ServiceBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

@Service("ShipmentIndexDocument")
public class ShipmentIndexDocument implements AbstractService<InputOutputParams, InputOutputParams> {
    @Autowired
    private ElasticsearchClient client;
    @Autowired
    private ServiceBus serviceBus;

    private static final String INDEX_NAME = "shipment";

    public List<ShipmentES> getShipmentsByCustomerId(ShipmentES shipmentES) {
        try {
            SearchResponse<ShipmentES> searchResponse = client.search(s -> s
                            .index(INDEX_NAME)
                            .query(q -> q
                                    .term(t -> t
                                            .field("customerId.keyword")
                                            .value(shipmentES.getCustomerId())
                                    )
                            ),
                    ShipmentES.class
            );

            return searchResponse.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ListValues runProcess (Context ctx, InputOutputParams input) {
        String methodName = ctx.getMethod();

        ObjectMapper mapper = new ObjectMapper();
        ShipmentES shipmentEs =  (ShipmentES) input;

        if (methodName.isBlank()) {
            throw new CustomizeException("Method is not Specified");
        }

        try {
            Method method = this.getClass().getMethod(methodName, ShipmentES.class);
            List<ShipmentES> result = (List<ShipmentES>) method.invoke(this, shipmentEs);
            ListValues listValues = new ListValues();
            listValues.setShipmentESList(result);
            return listValues;
        } catch (Exception ex) {
            throw new CustomizeException("Method not Found");
        }


    }

    public List<ShipmentES> creation(ShipmentES shipment) {
        try
        {
            IndexResponse response = client.index(i -> i
                    .index(INDEX_NAME)
                    .id(String.valueOf(shipment.getShipmentId()))
                    .document(shipment)
            );

            System.out.println("Status: " + response.result());

            return List.of(shipment);
        }
        catch (Exception e) {
            throw new CustomizeException("Shipment Data not Properly uploaded in the Elastic Search");
        }
    }
}
