package com.herts.shipment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.herts.model.*;
import com.herts.orchestrator.service.AbstractService;
import com.herts.orchestrator.service.ServiceBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("CustomerUpdate")
public class CustomerUpdate implements AbstractService<InputOutputParams, InputOutputParams> {

    @Autowired
    ServiceBus serviceBus;

    @Override
    public InputOutputParams runProcess(Context ctx, InputOutputParams input) {
        ObjectMapper mapper = new ObjectMapper();
        Customer customer = mapper.convertValue(input.getData(), Customer.class);

        ctx.setMethod("updating");

        serviceBus.execute("CustomerCreationDataService",ctx,customer);

        return customer;
    }

}
