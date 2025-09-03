package com.herts.shipment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.herts.model.*;
import com.herts.orchestrator.service.AbstractService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Service;

@Service("PaymentCreation")
public class PaymentCreation implements AbstractService<InputOutputParams, InputOutputParams> {
    @Override
    public InputOutputParams runProcess(Context ctx, InputOutputParams input) {
        ObjectMapper mapper = new ObjectMapper();
        PaymentRequest request = mapper.convertValue(input.getData(), PaymentRequest.class);

        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount((long) request.getAmount())
                    .setCurrency(request.getCurrency())
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);

            PaymentResponse payment = new PaymentResponse();
            payment.setClientSecret(intent.getClientSecret());
            return payment;
        } catch (StripeException e) {
            e.printStackTrace();
        }

        return null;
    }
}
