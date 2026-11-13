package com.herts.shipment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.herts.model.*;
import com.herts.orchestrator.service.AbstractService;
import com.herts.orchestrator.service.ServiceBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ShipmentStatusUpdate")
public class ShipmentStatusUpdate implements AbstractService<InputOutputParams, InputOutputParams> {
    @Autowired
    ServiceBus serviceBus;

    @Override
    public InputOutputParams runProcess(Context ctx, InputOutputParams input) {
        ObjectMapper mapper = new ObjectMapper();

        Shipment shipment = mapper.convertValue(input.getData(), Shipment.class);

        Offer offer = new Offer();
        offer.setShipmentId(shipment.getShipmentId());

        ctx.setMethod("getDataId");
        Offer offerDB;
        offerDB = (Offer) serviceBus.execute("OfferDataService", ctx, offer);
        offerDB.setStatus(shipment.getStatus());

        ctx.setMethod("updating");
        offerDB = (Offer) serviceBus.execute("OfferDataService", ctx, offerDB);

        ctx.setMethod("get");
        shipment = (Shipment) serviceBus.execute("ShipmentDataService", ctx, shipment);

        Order order = new Order();

        order.setShipment(shipment);
        order.setOfferList(List.of(offerDB));

        return order;
    }
}
