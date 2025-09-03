package com.herts.shipment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.herts.model.*;
import com.herts.orchestrator.service.AbstractService;
import com.herts.orchestrator.service.ServiceBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service("OrderConfirmation")
public class OrderConfirmation implements AbstractService<InputOutputParams, InputOutputParams> {

    @Autowired
    ServiceBus serviceBus;

    @Override
    public InputOutputParams runProcess(Context ctx, InputOutputParams input) {

        ObjectMapper mapper = new ObjectMapper();

        Offer offer = mapper.convertValue(input.getData(), Offer.class);

        Shipment shipment = new Shipment();
        shipment.setShipmentId(offer.getShipmentId());

        ctx.setMethod("get");
        shipment = (Shipment) serviceBus.execute("ShipmentDataService", ctx, shipment);

        shipment.setTrackingId(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6));
        shipment.setPickupNumber(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6));

        ctx.setMethod("update");
        shipment = (Shipment) serviceBus.execute("ShipmentDataService", ctx, shipment);

        ctx.setMethod("delete");
        serviceBus.execute("OfferDataService", ctx, offer);

        ctx.setMethod("get");
        Offer offerDB;
        offerDB = (Offer) serviceBus.execute("OfferDataService", ctx, offer);
        offer.setStatus("Yet to Pay");

        ctx.setMethod("updating");
        offerDB = (Offer) serviceBus.execute("OfferDataService", ctx, offerDB);

        Order  order = new Order();

        order.setOfferList(List.of(offerDB));
        order.setShipment(shipment);

        return order;
    }
}
