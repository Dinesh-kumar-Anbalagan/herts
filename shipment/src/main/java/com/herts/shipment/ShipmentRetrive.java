package com.herts.shipment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.herts.model.*;
import com.herts.orchestrator.service.AbstractService;
import com.herts.orchestrator.service.ServiceBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("ShipmentRetrive")
public class ShipmentRetrive implements AbstractService<InputOutputParams, InputOutputParams> {
    @Autowired
    ServiceBus serviceBus;

    @Override
    public InputOutputParams runProcess(Context ctx, InputOutputParams input) {
        ObjectMapper mapper = new ObjectMapper();

        ShipmentES shipmentES = mapper.convertValue(input.getData(), ShipmentES.class);

        Shipment shipment = new Shipment();
        shipment.setShipmentId(shipmentES.getShipmentId());

        ctx.setMethod("get");
        shipment = (Shipment) serviceBus.execute("ShipmentDataService", ctx, shipment);

        Offer offer = new Offer();
        offer.setShipmentId(shipmentES.getShipmentId());

        ctx.setMethod("getDataId");
        Offer offerDB;
        offerDB = (Offer) serviceBus.execute("OfferDataService", ctx, offer);

        Vendor vendor = new Vendor();
        vendor.setVendorId(offerDB.getVendorId());
        ctx.setMethod("findById");
        Vendor vendorDB;
        ListValues list = (ListValues) serviceBus.execute("VendorDataService", ctx, vendor);
        vendorDB = list.getVendorList().get(0);

        Customer customer = new Customer();
        customer.setCustomerID(offerDB.getCustomerID());
        ctx.setMethod("getDataId");
        Customer customerDB;
        customerDB = (Customer) serviceBus.execute("CustomerCreationDataService", ctx, customer);

        ShipmentResponse response = new ShipmentResponse();

        response.setCarrierContact(vendorDB.getPhoneNumber());
        response.setPackageCount(1);
        response.setTrackingNumber(shipment.getTrackingId());
        response.setPickupNo(shipment.getPickupNumber());
        response.setCarrierLogo(vendorDB.getVendorName()+"_logo");
        response.setCarrierName(vendorDB.getVendorName());
        response.setEstimatedDelivery(offerDB.getEstimateDelivery());
        response.setStatus(offerDB.getStatus());
        response.setFrom(shipment.getSender());
        response.setTo(shipment.getReceiver());
        response.setWeight(shipment.getHandlingUnit().getWeight());
        response.setCost(offerDB.getOfferCost());
        response.setOfferId(offerDB.getOfferId());
        response.setCustomerName(customerDB.getLastName());
        response.setShipmentId(shipment.getShipmentId());
        response.setCustomerId(shipment.getCustomerId());


        List<String> status = new ArrayList<>();
              status = List.of("Yet to Pay", "Created", "In_Transit", " Out For Delivery", "Delivered" );

        ArrayList<String> progress = new ArrayList<>();

        for (String stat : status)
        {
            if(stat.equalsIgnoreCase(offerDB.getStatus()))
            {
                progress.add(stat);
                break;
            }

            progress.add(stat);
        }
        response.setProgress(progress);

        return response;
    }
}
