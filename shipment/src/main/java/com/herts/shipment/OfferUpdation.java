package com.herts.shipment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.herts.model.Context;
import com.herts.model.InputOutputParams;
import com.herts.model.Offer;
import com.herts.orchestrator.service.AbstractService;
import com.herts.orchestrator.service.ServiceBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("OfferUpdation")
public class OfferUpdation  implements AbstractService<InputOutputParams, InputOutputParams> {
    @Autowired
    ServiceBus serviceBus;

    @Override
    public InputOutputParams runProcess(Context ctx, InputOutputParams input) {
        ObjectMapper mapper = new ObjectMapper();
        Offer offer = mapper.convertValue(input.getData(), Offer.class);

        ctx.setMethod("get");
        Offer offerDB;
        offerDB = (Offer) serviceBus.execute("OfferDataService", ctx, offer);
        offerDB.setStatus(offer.getStatus());

        ctx.setMethod("updating");
        offerDB = (Offer) serviceBus.execute("OfferDataService", ctx, offerDB);

        return offerDB;
    }
}
