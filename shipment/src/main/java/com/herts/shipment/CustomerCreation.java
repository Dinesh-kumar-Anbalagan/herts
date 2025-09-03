package com.herts.shipment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.herts.model.*;
import com.herts.orchestrator.service.AbstractService;
import com.herts.orchestrator.service.ServiceBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("CustomerCreation")
public class CustomerCreation implements AbstractService<InputOutputParams, InputOutputParams> {

    @Autowired
    ServiceBus serviceBus;

    @Override
    public InputOutputParams runProcess(Context ctx, InputOutputParams input) {
        ObjectMapper mapper = new ObjectMapper();
        Customer customer = mapper.convertValue(input.getData(), Customer.class);

        ctx.setMethod("retrieve");
        Customer customer1 = (Customer) serviceBus.execute("CustomerCreationDataService",ctx,customer);
        if(customer1==null) {
            ctx.setMethod("creation");
            String hashedPassword = PasswordUtil.hashPassword(customer.getPassword());
            customer.setPassword(hashedPassword);
            customer.setAccountNumber("W00" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 4));
            customer.setCustomerID(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8));
            customer.setStatus("Active");

            serviceBus.execute("CustomerCreationDataService", ctx, customer);

            return customer;
        }
        else
        {
            throw new RuntimeException("Customer Already Exists");
        }

    }

}
