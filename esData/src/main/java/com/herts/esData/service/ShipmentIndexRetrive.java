package com.herts.esData.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.herts.model.*;
import com.herts.orchestrator.service.AbstractService;
import com.herts.orchestrator.service.ServiceBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ShipmentIndexRetrive")
public class ShipmentIndexRetrive implements AbstractService<InputOutputParams, InputOutputParams> {
    @Autowired
    ServiceBus serviceBus;

    @Override
    public InputOutputParams runProcess(Context ctx, InputOutputParams input) {
        ObjectMapper mapper = new ObjectMapper();
        Customer customer = mapper.convertValue(input.getData(), Customer.class);

        ShipmentES shipmentES = new ShipmentES();
        shipmentES.setCustomerId(customer.getCustomerID());

        ctx.setMethod("getShipmentsByCustomerId");
        ListValues listValues = (ListValues) serviceBus.execute("ShipmentIndexDocument",ctx,shipmentES);

        return listValues;
    }
}
