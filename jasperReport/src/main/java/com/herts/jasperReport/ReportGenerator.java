package com.herts.jasperReport;

import com.herts.model.*;
import com.herts.orchestrator.service.AbstractService;
import com.herts.orchestrator.service.ServiceBus;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("ReportGenerator")
public class ReportGenerator implements AbstractService<InputOutputParams, InputOutputParams>
{
    @Autowired
    ServiceBus serviceBus;

    private static Map<String, Object> createItem(String vendorName, String vendorContact, double weight, double cost)
    {
        Map<String, Object> item = new HashMap<>();
        item.put("VendorName", vendorName);
        item.put("VendorContact", vendorContact);
        item.put("Weight", weight);
        item.put("Amount", cost);
        return item;
    }

    @Override
    public InputOutputParams runProcess(Context ctx, InputOutputParams input)
    {
        ShipmentResponse shipmentResponse = (ShipmentResponse) input;
        try
        {
            InputStream reportStream = getClass().getResourceAsStream("/demo.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            List<Map<String, Object>> items = new ArrayList<>();
            items.add(createItem(shipmentResponse.getCarrierName(), shipmentResponse.getCarrierContact(),
                    shipmentResponse.getWeight(), shipmentResponse.getCost()));

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(items);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("InvoiceNumber", UUID.randomUUID().toString().substring(0, 8));
            parameters.put("InvoiceDate", new Date());
            parameters.put("DueDate", new Date());
            parameters.put("CustomerName", shipmentResponse.getCustomerName());

            Address from = shipmentResponse.getFrom();

            String address = Stream.of(from.getAddress(), from.getLocality(), from.getCounty(),
                            from.getPostalCode(), from.getCountry())
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining(", "));

            parameters.put("CustomerAddress", address);
            parameters.put("PickUpNumber", shipmentResponse.getPickupNo());
            parameters.put("TrackingId", shipmentResponse.getTrackingNumber());

            double tax = shipmentResponse.getCost() * (0.15/100);
            double subtotal = shipmentResponse.getCost()-tax;
            double total = subtotal + tax;

            parameters.put("Tax", tax);
            parameters.put("Total", total);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

            byte[] pdfBytes = outputStream.toByteArray();
            String base64Pdf = Base64.getEncoder().encodeToString(pdfBytes);

            Report report = new Report();
            report.setBase64(base64Pdf);
            report.setShipmentId(shipmentResponse.getShipmentId());
            report.setOfferId(shipmentResponse.getOfferId());
            report.setCustomerID(shipmentResponse.getCustomerId());

            ctx.setMethod("creation");
            serviceBus.execute("ReportDataService",ctx,report);

            ReportResponse reportResponse = new ReportResponse();

            reportResponse.setBase64(report.getBase64());
            reportResponse.setFilename(report.getShipmentId()+"_invoice.pdf");

            return reportResponse;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}