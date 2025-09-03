package com.herts.shipment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.herts.model.Context;
import com.herts.model.Customer;
import com.herts.model.InputOutputParams;
import com.herts.orchestrator.service.AbstractService;
import com.herts.orchestrator.service.ServiceBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("CustomerFetch")
public class CustomerFetch implements AbstractService<InputOutputParams, InputOutputParams> {

    @Autowired
    ServiceBus serviceBus;

    @Override
    public InputOutputParams runProcess(Context ctx, InputOutputParams input) {
        ObjectMapper mapper = new ObjectMapper();
        Customer customer = mapper.convertValue(input.getData(), Customer.class);

        ctx.setMethod("retrieve");

        Customer customerDb = (Customer) serviceBus.execute("CustomerCreationDataService",ctx,customer);

        if (PasswordUtil.checkPassword(customer.getPassword(), customerDb.getPassword())) {
            return customerDb;
        } else {
            return null;
        }
    }
}