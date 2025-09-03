package com.herts.shipment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.herts.model.*;
import com.herts.orchestrator.exception.CustomizeException;
import com.herts.orchestrator.service.AbstractService;
import com.herts.orchestrator.service.ServiceBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service("OfferCreation")
public class OfferCreation implements AbstractService<InputOutputParams, InputOutputParams> {

    @Autowired
    ServiceBus serviceBus;

    @Override
    public InputOutputParams runProcess(Context ctx, InputOutputParams input) {

        ListValues listValues = new ListValues();
        Order order = new Order();

        ObjectMapper mapper = new ObjectMapper();
        Shipment shipment = mapper.convertValue(input.getData(), Shipment.class);

        shipment.setShipmentId(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6));

        ctx.setMethod("insert");
        serviceBus.execute("ShipmentDataService", ctx, shipment);

        listValues = (ListValues) serviceBus.execute("LTLOfferCreation",ctx,shipment);

        order.setOfferList(listValues.getOfferList());
        order.setShipment(shipment);

        ctx.setMethod("insert");

        return order;
    }
}
