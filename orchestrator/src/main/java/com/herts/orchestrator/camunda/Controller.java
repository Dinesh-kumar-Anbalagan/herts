package com.herts.orchestrator.camunda;

import com.herts.model.Context;
import com.herts.model.InputOutputParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {


    @Autowired
    CamundaStarter camundaStarter;

    @PostMapping("/{flowName}")
    public InputOutputParams create_Order(@PathVariable String flowName, @RequestBody Context request) {
        return camundaStarter.startOrderProcess(flowName, request);
    }
}
