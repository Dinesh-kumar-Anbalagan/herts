package com.herts.orchestrator.service;

import com.herts.model.Context;
import com.herts.model.InputOutputParams;
import com.herts.orchestrator.bean.DynamicBeanRegistrar;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@ComponentScan
public class ServiceBus {
    @Autowired
    private ApplicationContext context;

    @Autowired
    private DynamicBeanRegistrar dynamicBeanRegistrar;

    public InputOutputParams execute(String serviceName, Context ctx, InputOutputParams request) {
       boolean small =  context.containsBeanDefinition("reportGenerator");
        boolean cap =  context.containsBeanDefinition("ReportGenerator");

        AbstractService service = (AbstractService) context.getBean(serviceName);

        if (service != null) {
          return (InputOutputParams) service.runProcess( ctx,request);
        } else {
            throw new IllegalArgumentException("Unknown service: " + serviceName);
        }
    }
}
