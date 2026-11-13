package com.herts.orchestrator.service;

import com.herts.model.Context;
import com.herts.model.InputOutputParams;
import com.herts.orchestrator.bean.SpringContextBridge;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import java.util.HashMap;
import java.util.Map;

public class ServiceTask implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        ServiceBus serviceBus = SpringContextBridge.getBean(ServiceBus.class);

        String className = (String) execution.getVariable("service");

        Object ctxObj = execution.getVariable("ctx");

        Context context;

        if (ctxObj instanceof Context) {
            context = (Context) ctxObj;
        } else if (ctxObj instanceof Map) {
            // Wrap the map into your Context wrapper
            context = new Context((Map<String, Object>) ctxObj);
        } else {
            throw new RuntimeException("Unexpected ctx variable type: " + ctxObj.getClass());
        }

        Object inputObj = execution.getVariable("input");
        InputOutputParams input;

        if (inputObj instanceof InputOutputParams) {
            input = (InputOutputParams) inputObj;
        } else if (inputObj instanceof Map) {
            // Wrap the map into your Context wrapper
            input = new InputOutputParams((Map<String, Object>) inputObj) {
            };
        } else if (inputObj instanceof HashMap) {
            // Wrap the map into your Context wrapper
            input = new InputOutputParams((HashMap<String, Object>) inputObj) {
            };
        }else {
            throw new RuntimeException("Unexpected ctx variable type: " + ctxObj.getClass());
        }

       InputOutputParams result =  serviceBus.execute(className, context,input);
        execution.setVariable("result", result);
    }
}


