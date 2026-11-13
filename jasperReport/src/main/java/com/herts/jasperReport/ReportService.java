package com.herts.jasperReport;

import com.herts.model.Address;
import com.herts.model.Context;
import com.herts.model.Customer;
import com.herts.orchestrator.service.AbstractService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;

@Service
public class ReportService implements AbstractService<Address,String> {
    public byte[] generateReport() throws Exception {
        InputStream reportStream = getClass().getResourceAsStream("/demo.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        Customer customer = new Customer();

        customer.setCustomerID("KAIDJALS");

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(List.of(customer));

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Spring Boot App");

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

        return outputStream.toByteArray();
    }

    @Override
    public String runProcess(Context ctx, Address input) {
        ReportService reportService = new ReportService();
        try{
            System .out.println(reportService.generateReport());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}

