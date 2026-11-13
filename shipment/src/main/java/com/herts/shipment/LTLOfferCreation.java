package com.herts.shipment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.herts.model.*;
import com.herts.orchestrator.service.AbstractService;
import com.herts.orchestrator.service.ServiceBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LTLOfferCreation implements AbstractService<Shipment, InputOutputParams> {
    @Autowired
    ServiceBus serviceBus;

    @Autowired
    DistanceMatrixService distanceMatrixService;

    @Override
    public ListValues runProcess(Context ctx, Shipment input) {
        ctx.setMethod("retrieve");

        ListValues vendorList = (ListValues) serviceBus.execute("VendorDataService", ctx, new Vendor());

        List<Offer> offerList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        List<Vendor> vendors = new ArrayList<>();

        for (Object raw : vendorList.getList()) {
            Vendor vendor = mapper.convertValue(raw, Vendor.class);
            vendors.add(vendor);
        }

        ctx.setMethod("creation");

        double distance;

        try {
            double[] originCords = distanceMatrixService.getCoordinates(input.getSender().getPostalCode());
            double[] destCords = distanceMatrixService.getCoordinates(input.getReceiver().getPostalCode());

            distance = distanceMatrixService.calculateDistanceKm(originCords, destCords);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for(Vendor vendor: vendors) {
            Offer offer = new Offer();

            offer.setOfferId(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6));
            offer.setVendorName(vendor.getVendorName());
            offer.setVendorId(vendor.getVendorId());
            offer.setVendorEmail(vendor.getEmailId());
            offer.setOfferDate(LocalDate.now().toString());
            offer.setExpriationDate(LocalDate.now().plusDays(7).toString());
            offer.setStatus("Created");
            offer.setCustomerID(input.getCustomerId());
            offer.setShipmentId(input.getShipmentId());

            this.estimateDeliveryDate(distance,vendor, offer);
            serviceBus.execute("OfferDataService", ctx, offer);
            offerList.add(offer);
        }

        ListValues offerLists = new ListValues();

        offerLists.setOfferList(offerList);

        return offerLists;
    }

    public LocalDate estimateDeliveryDate(double distance, Vendor vendor, Offer offer)  {

        int estimatedDays = Math.max(1, (int) Math.ceil(distance / vendor.getMindist()));

        offer.setEstimateDelivery(String.valueOf(LocalDate.now().plusDays(estimatedDays)));
        offer.setOfferCost((Math.round((distance* vendor.getDistCost()+(estimatedDays*vendor.getMinCost()))*0.04)*100.0)/100.0);
        return LocalDate.now().plusDays(estimatedDays);
    }
}
