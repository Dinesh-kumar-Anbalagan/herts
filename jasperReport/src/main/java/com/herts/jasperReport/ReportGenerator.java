package com.herts.jasperReport;
import com.fasterxml.jackson.databind.ObjectMapper;
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
            // 1. Compile the JRXML file
            InputStream reportStream = getClass().getResourceAsStream("/demo.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // 2. Prepare data (list of items in the table)
            List<Map<String, Object>> items = new ArrayList<>();
            items.add(createItem(shipmentResponse.getCarrierName(), shipmentResponse.getCarrierContact(), shipmentResponse.getWeight(), shipmentResponse.getCost()));

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(items);

            // 3. Fill parameters
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("InvoiceNumber", UUID.randomUUID().toString().substring(0, 8));
            parameters.put("InvoiceDate", new Date());
            parameters.put("DueDate", new Date());
            parameters.put("CustomerName", shipmentResponse.getCustomerName());

            Address from = shipmentResponse.getFrom();

            String address = Stream.of(from.getAddress(), from.getLocality(), from.getCounty(), from.getPostalCode(), from.getCountry())
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining(", ")); // Join with comma and space

            parameters.put("CustomerAddress", address);
            parameters.put("PickUpNumber", shipmentResponse.getPickupNo());
            parameters.put("TrackingId", shipmentResponse.getTrackingNumber());


            double tax = shipmentResponse.getCost() * (0.15/100);
            double subtotal = shipmentResponse.getCost()-tax;
            double total = subtotal + tax;

            parameters.put("Tax", tax);
            parameters.put("Total", total);

            // 4. Fill the report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            // 5. Export to PDF
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

            // 6. Convert PDF to Base64
            byte[] pdfBytes = outputStream.toByteArray();
            String base64Pdf = Base64.getEncoder().encodeToString(pdfBytes);

            System.out.println("Base64 PDF:");
            System.out.println(base64Pdf);

            System.out.println("Invoice generated successfully!");

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


/*

package com.herts.jasperReport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.herts.model.Context;
import com.herts.model.InputOutputParams;
import com.herts.model.ShipmentResponse;
import com.herts.orchestrator.service.AbstractService;
import net.sf.jasperreports.engine.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;

@Service("ReportGenerator")
public class ReportGenerator implements AbstractService<InputOutputParams, InputOutputParams> {



    @Override
    public InputOutputParams runProcess(Context ctx, InputOutputParams input) {

        ObjectMapper mapper = new ObjectMapper();
        ShipmentResponse shipmentResponse = mapper.convertValue(input.getData(), ShipmentResponse.class);

        try {
            // 1. Load and compile the JRXML template
            InputStream reportStream = getClass().getResourceAsStream("/demo.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // 2. Prepare parameters
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("InvoiceNumber", UUID.randomUUID().toString().substring(0, 8));
            parameters.put("InvoiceDate", new Date());
            parameters.put("DueDate", new Date());
            parameters.put("CustomerName", shipmentResponse.getCustomerName());
            parameters.put("CustomerAddress", shipmentResponse.getFrom());
            parameters.put("PickUpNumber", shipmentResponse.getPickupNo());
            parameters.put("TrackingId", shipmentResponse.getTrackingNumber());
            parameters.put("VendorName", shipmentResponse.getCarrierName());
            parameters.put("VendorContact", shipmentResponse.getCarrierContact());
            parameters.put("Weight", shipmentResponse.getWeight());

            double subtotal = shipmentResponse.getCost();
            double tax = subtotal * 0.15;
            double total = subtotal + tax;

            parameters.put("Amount", subtotal);
            parameters.put("Tax", tax);
            parameters.put("Total", total);

            // 3. Fill the report (no data source needed)
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters);

            // 4. Export report to PDF
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

            // 5. Convert to Base64
            byte[] pdfBytes = outputStream.toByteArray();
            String base64Pdf = Base64.getEncoder().encodeToString(pdfBytes);

            System.out.println("Invoice generated successfully (Base64):");
            System.out.println(base64Pdf);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}









<?xml version="1.0" encoding="UTF-8"?>
<!-- Created with Jaspersoft Studio version 6.21.5.final using JasperReports Library version 6.21.5-74d586df47b25dbd05bd0957999819196e59934a  -->
<jasperReport xmlns="http://jasperreports.sourceforge.net/jasperreports" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd" name="ShippingInvoice" pageWidth="595" pageHeight="842" columnWidth="555" leftMargin="20" rightMargin="20" topMargin="20" bottomMargin="20" uuid="e56fe0a3-768b-4633-8539-08a66e620aac">
	<parameter name="InvoiceNumber" class="java.lang.String"/>
	<parameter name="InvoiceDate" class="java.util.Date"/>
	<parameter name="DueDate" class="java.util.Date"/>
	<parameter name="CustomerName" class="java.lang.String"/>
	<parameter name="CustomerAddress" class="java.lang.String"/>
	<parameter name="PickUpNumber" class="java.lang.String"/>
	<parameter name="TrackingId" class="java.lang.Double"/>
	<parameter name="Tax" class="java.lang.Double"/>
	<parameter name="Total" class="java.lang.Double"/>
	<parameter name="VendorName" class="java.lang.String"/>
	<parameter name="VendorContact" class="java.lang.String"/>
	<parameter name="Weight" class="java.lang.Double"/>
	<parameter name="Amount" class="java.lang.Double"/>
	<title>
		<band height="120">
			<staticText>
				<reportElement x="0" y="0" width="200" height="20" uuid="7baf2045-4c4c-4b26-a391-f0280d14839d"/>
				<textElement>
					<font size="14" isBold="true"/>
				</textElement>
				<text><![CDATA[HERTsSHIPMENT]]></text>
			</staticText>
			<staticText>
				<reportElement x="0" y="20" width="200" height="20" uuid="eacc34cd-6ebf-40c1-bdbc-453ffda93c4f"/>
				<text><![CDATA[College Lane, Hatfield, AL10 9AB]]></text>
			</staticText>
			<staticText>
				<reportElement x="400" y="0" width="150" height="20" uuid="d2e8cf87-ec0a-4f7e-928a-ec3b92f89118"/>
				<textElement textAlignment="Right">
					<font size="16" isBold="true"/>
				</textElement>
				<text><![CDATA[SHIPPING INVOICE]]></text>
			</staticText>
			<staticText>
				<reportElement x="400" y="40" width="100" height="20" uuid="35ad2d5e-9a6e-45e3-a8a4-3f48c2f1a986"/>
				<text><![CDATA[Invoice #:]]></text>
			</staticText>
			<textField>
				<reportElement x="500" y="40" width="70" height="20" uuid="0bc09057-0902-4655-b3d1-bac2022c7f2e"/>
				<textFieldExpression><![CDATA[$P{InvoiceNumber}]]></textFieldExpression>
			</textField>
			<staticText>
				<reportElement x="400" y="60" width="100" height="20" uuid="df07c9b3-93fd-4f4d-bbd8-23222b9fa844"/>
				<text><![CDATA[Invoice Date:]]></text>
			</staticText>
			<textField pattern="MM-dd-yyyy">
				<reportElement x="500" y="60" width="70" height="20" uuid="fe2436d7-75ab-4e7d-9c51-4d97f6f10e91"/>
				<textFieldExpression><![CDATA[$P{InvoiceDate}]]></textFieldExpression>
			</textField>
			<staticText>
				<reportElement x="400" y="80" width="100" height="20" uuid="3a685121-2bfb-4c4f-9019-4bb8a635ccfa"/>
				<text><![CDATA[Due Date:]]></text>
			</staticText>
			<textField pattern="MM-dd-yyyy">
				<reportElement x="500" y="80" width="70" height="20" uuid="1815e9ff-e342-4ad6-a457-05cf8f3f843f"/>
				<textFieldExpression><![CDATA[$P{DueDate}]]></textFieldExpression>
			</textField>
			<staticText>
				<reportElement x="0" y="50" width="100" height="20" uuid="77b579ef-55a4-4b07-8bcb-bc648875e7cf"/>
				<textElement>
					<font isBold="true"/>
				</textElement>
				<text><![CDATA[Bill To]]></text>
			</staticText>
			<textField>
				<reportElement x="0" y="70" width="250" height="20" uuid="1e9f5bc5-9463-4862-b962-2f8132ef43fc"/>
				<textFieldExpression><![CDATA[$P{CustomerName}]]></textFieldExpression>
			</textField>
			<textField>
				<reportElement x="0" y="90" width="250" height="20" uuid="e191509b-4a61-47b6-bd64-8e0ea7c88b63"/>
				<textFieldExpression><![CDATA[$P{CustomerAddress}]]></textFieldExpression>
			</textField>
			<staticText>
				<reportElement x="400" y="100" width="100" height="20" uuid="45a8e6eb-4c44-487e-850e-6b980a88e240"/>
				<text><![CDATA[Tracking ID:]]></text>
			</staticText>
			<textField pattern="MM-dd-yyyy">
				<reportElement x="500" y="100" width="70" height="20" uuid="eaee8f4a-7e5b-47c6-9e0f-3e12c810223c"/>
				<textFieldExpression><![CDATA[$P{TrackingId}]]></textFieldExpression>
			</textField>
		</band>
	</title>
	<pageHeader>
		<band height="80">
			<textField pattern="MM-dd-yyyy">
				<reportElement x="500" y="0" width="70" height="20" uuid="b7a6c46e-048b-4663-9765-07c083e89f31"/>
				<textFieldExpression><![CDATA[$P{PickUpNumber}]]></textFieldExpression>
			</textField>
			<staticText>
				<reportElement x="400" y="0" width="100" height="20" uuid="38c94b6c-4e2e-440e-9a5e-92ac8d54851f"/>
				<text><![CDATA[PickUpNumber:]]></text>
			</staticText>
		</band>
	</pageHeader>
	<columnHeader>
		<band height="20">
			<staticText>
				<reportElement x="0" y="-47" width="180" height="20" backcolor="#CCCCCC" uuid="15bb8720-bc27-45e6-bf58-d30079957e89"/>
				<textElement textAlignment="Center" verticalAlignment="Middle">
					<font isBold="true"/>
				</textElement>
				<text><![CDATA[Vendor Name]]></text>
			</staticText>
			<staticText>
				<reportElement x="180" y="-47" width="170" height="20" backcolor="#CCCCCC" uuid="ce94f9fa-f8a2-4d4e-a172-460a0420f6aa"/>
				<textElement textAlignment="Center" verticalAlignment="Middle">
					<font isBold="true"/>
				</textElement>
				<text><![CDATA[Vendor Contact]]></text>
			</staticText>
			<staticText>
				<reportElement x="350" y="-47" width="100" height="20" backcolor="#CCCCCC" uuid="11e6b121-04ed-40b2-a021-4575c65a760d"/>
				<textElement textAlignment="Center" verticalAlignment="Middle">
					<font isBold="true"/>
				</textElement>
				<text><![CDATA[Weight]]></text>
			</staticText>
			<staticText>
				<reportElement x="450" y="-47" width="100" height="20" backcolor="#CCCCCC" uuid="3136c816-727b-4642-b3a7-8ffbdece5b29"/>
				<textElement textAlignment="Center" verticalAlignment="Middle">
					<font isBold="true"/>
				</textElement>
				<text><![CDATA[Amount]]></text>
			</staticText>
		</band>
	</columnHeader>
	<detail>
		<band height="20">
			<textField>
				<reportElement x="0" y="-41" width="180" height="20" uuid="9a4c4e93-5f1e-4348-a01a-baa23a0d8674"/>
				<textElement textAlignment="Center"/>
				<textFieldExpression><![CDATA[$P{VendorName}]]></textFieldExpression>
			</textField>
			<textField>
				<reportElement x="180" y="-41" width="170" height="20" uuid="2e3e2a10-5df5-46a6-b502-c2debf997490"/>
				<textElement textAlignment="Center"/>
				<textFieldExpression><![CDATA[$P{VendorContact}]]></textFieldExpression>
			</textField>
			<textField pattern="$###,##0.00">
				<reportElement x="350" y="-41" width="100" height="20" uuid="95c8e27e-0a2f-4f92-b0f7-690e13c31d32"/>
				<textElement textAlignment="Center"/>
				<textFieldExpression><![CDATA[$P{Weight}]]></textFieldExpression>
			</textField>
			<textField pattern="$###,##0.00">
				<reportElement x="450" y="-41" width="100" height="20" uuid="1e99e5f5-83d6-4b7a-95bb-1ec331f6b1be"/>
				<textElement textAlignment="Center"/>
				<textFieldExpression><![CDATA[$P{Amount}]]></textFieldExpression>
			</textField>
		</band>
	</detail>
	<summary>
		<band height="60">
			<staticText>
				<reportElement x="350" y="-29" width="100" height="20" uuid="6a75e5fc-c042-4556-9e2b-c71db1138464"/>
				<textElement textAlignment="Center"/>
				<text><![CDATA[Sales Tax (15%)]]></text>
			</staticText>
			<textField pattern="$###,##0.00">
				<reportElement x="450" y="-29" width="100" height="20" uuid="e69e41b7-36ce-4b04-a4e9-76e2e9e77c1e"/>
				<textElement textAlignment="Center"/>
				<textFieldExpression><![CDATA[$P{Tax}]]></textFieldExpression>
			</textField>
			<staticText>
				<reportElement x="350" y="-9" width="100" height="20" uuid="5b8a6f5e-f261-4068-87ab-4a6b1d72c2cd"/>
				<textElement textAlignment="Center">
					<font isBold="true"/>
				</textElement>
				<text><![CDATA[Total (USD)]]></text>
			</staticText>
			<textField pattern="$###,##0.00">
				<reportElement x="450" y="-9" width="100" height="20" uuid="1d5c17c1-28e4-4fbe-9487-bf8ff372d155"/>
				<textElement textAlignment="Center">
					<font isBold="true"/>
				</textElement>
				<textFieldExpression><![CDATA[$P{Total}]]></textFieldExpression>
			</textField>
		</band>
	</summary>
</jasperReport>

 */