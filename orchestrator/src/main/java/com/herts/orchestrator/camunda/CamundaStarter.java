package com.herts.orchestrator.camunda;



import com.herts.model.Context;
import com.herts.model.InputOutputParams;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessInstanceWithVariablesImpl;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Lazy
@Component
public class CamundaStarter {

    @Autowired
    private ProcessEngine processEngine;

    public InputOutputParams startOrderProcess(String processName, Context ctxt) {
        String processNameDeploy = processName+".bpmn";

        Deployment deployment = processEngine.getRepositoryService()
                .createDeployment()
                .addClasspathResource(processNameDeploy)
                .deploy();

        Map<String,Object> data = new HashMap<>();
        Map<String,Object> ctx = new HashMap<>();
        ctx.put("request", ctxt.getAll());
        data.put("ctx", ctx.get("request"));

        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processName,data);

        System.out.println("🚀 Started process instance with ID: " + processInstance.getId());

        return (InputOutputParams) ((ProcessInstanceWithVariablesImpl) processInstance).getVariables().get("result");
    }
}
