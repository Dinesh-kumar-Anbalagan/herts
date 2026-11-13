package com.herts.esData.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.herts.model.*;
import com.herts.orchestrator.service.AbstractService;
import com.herts.orchestrator.service.ServiceBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ShipmentIndexCreation")
public class ShipmentIndexCreation implements AbstractService<InputOutputParams, InputOutputParams> {
    @Autowired
    ServiceBus serviceBus;

    @Override
    public InputOutputParams runProcess(Context ctx, InputOutputParams input) {
        ObjectMapper mapper = new ObjectMapper();

        Order order = (Order) input;

        Shipment shipment = order.getShipment();
        Offer offer = order.getOfferList().get(0);

        ShipmentES shipmentES = new ShipmentES();

        shipmentES.setSenderCompany(shipment.getSender().getAddress());
        shipmentES.setReceiverCompany(shipment.getReceiver().getAddress());
        shipmentES.setCarrier(offer.getVendorName());
        shipmentES.setShipDate(shipment.getShipmentDate());
        shipmentES.setSenderPostalCode(shipment.getSender().getPostalCode());
        shipmentES.setReceiverPostalCode(shipment.getReceiver().getPostalCode());
        shipmentES.setTracking(shipment.getTrackingId());
        shipmentES.setStatus(offer.getStatus());
        shipmentES.setEstimatedDelivery(offer.getEstimateDelivery());
        shipmentES.setPickupNumber(shipment.getPickupNumber());
        shipmentES.setCustomerId(offer.getCustomerID());
        shipmentES.setShipmentId(offer.getShipmentId());
        shipmentES.setTracking(shipment.getTrackingId());
        shipmentES.setPickupNumber(shipment.getPickupNumber());

        ctx.setMethod("creation");
        serviceBus.execute("ShipmentIndexDocument",ctx,shipmentES);

        return shipmentES;
    }
}
