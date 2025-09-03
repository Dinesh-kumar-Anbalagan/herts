package com.herts.dbService.service;

import com.herts.dbService.mybatis.MybatisMapper;

import com.herts.model.*;
import com.herts.orchestrator.exception.CustomizeException;
import com.herts.orchestrator.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.List;

@Service
public class OrderDataService implements AbstractService<Shipment, InputOutputParams> {

    @Autowired
    MybatisMapper mapper;

    @Override
    public ListValues runProcess(Context ctx, Shipment input) {

        String methodName = ctx.getMethod();

        if (methodName.isBlank()) {
            throw new CustomizeException("Method is not Specified");
        }

        try {
            Method method = this.getClass().getMethod(methodName);
            List<Vendor> vendorList = (List<Vendor>) method.invoke(this, input);

            ListValues arrayListValues = new ListValues();

            arrayListValues.setVendorList(vendorList);
            return arrayListValues;

        } catch (Exception ex) {
            throw new CustomizeException("Method not Found");
        }
    }

    public Shipment insert (Context ctx, Shipment shipment)
    {
        mapper.runQuery("Shipment", "insert", shipment);

        return null;
    }
}