package com.herts.jasperReport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.herts.model.*;
import com.herts.orchestrator.service.AbstractService;
import com.herts.orchestrator.service.ServiceBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ReportFetch")
public class ReportFetch implements AbstractService<InputOutputParams, InputOutputParams> {
    @Autowired
    ServiceBus serviceBus;

    @Override
    public InputOutputParams runProcess(Context ctx, InputOutputParams input) {
        ObjectMapper mapper = new ObjectMapper();
        Report report = mapper.convertValue(input.getData(), Report.class);

        ctx.setMethod("retrieve");
        report = (Report) serviceBus.execute("ReportDataService", ctx, report);

        ReportResponse reportResponse = new ReportResponse();
        reportResponse.setFilename(report.getShipmentId()+"_invoice.pdf");
        reportResponse.setBase64(report.getBase64());
        return reportResponse;
    }
}
