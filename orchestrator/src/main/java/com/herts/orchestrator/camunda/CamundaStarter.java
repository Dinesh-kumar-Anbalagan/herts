package com.herts.orchestrator.camunda;

import com.herts.model.Context;
import com.herts.model.InputOutputParams;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessInstanceWithVariablesImpl;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        HistoryService historyService = processEngine.getHistoryService();

        HistoricProcessInstance process =
                historyService.createHistoricProcessInstanceQuery()
                        .processInstanceId(processInstance.getProcessInstanceId())
                        .singleResult();

        // Activities (FLOW STEPS)
        List<HistoricActivityInstance> activities =
                historyService.createHistoricActivityInstanceQuery()
                        .processInstanceId(processInstance.getProcessInstanceId())
                        .orderByHistoricActivityInstanceStartTime()
                        .asc()
                        .list();

        System.out.println(process.getProcessDefinitionName()+" Execution Details : ");

        System.out.println("Start: " + process.getStartTime());
        System.out.println("End: " + process.getEndTime());
        System.out.println("Duration: " + process.getDurationInMillis());

        for (HistoricActivityInstance act : activities) {
            System.out.println("Step: " + act.getActivityId());
            System.out.println("Name: " + act.getActivityName());
            System.out.println("Type: " + act.getActivityType());
            System.out.println("Start: " + act.getStartTime());
            System.out.println("End: " + act.getEndTime());
            System.out.println("Duration: " + act.getDurationInMillis());
            System.out.println("----------------------");
        }

        InputOutputParams result;

        Object resultObj = ((ProcessInstanceWithVariablesImpl) processInstance).getVariables().get("result");

        if (resultObj instanceof InputOutputParams) {
            result = (InputOutputParams) resultObj;
        } else if (resultObj instanceof Map) {
            // Wrap the map into your Context wrapper
            result = new InputOutputParams((Map<String, Object>) resultObj) {
            };
        } else if (resultObj instanceof HashMap) {
            // Wrap the map into your Context wrapper
            result = new InputOutputParams((HashMap<String, Object>) resultObj) {
            };
        }else {
            throw new RuntimeException("Unexpected ctx variable type: " + resultObj.getClass());
        }

        return result;
    }
}
