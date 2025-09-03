package com.herts.dbService.service;

import com.herts.dbService.mybatis.MybatisMapper;
import com.herts.model.*;
import com.herts.orchestrator.exception.CustomizeException;
import com.herts.orchestrator.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.List;

@Service("ShipmentDataService")
public class ShipmentDataService implements AbstractService<Shipment, InputOutputParams> {
    @Autowired
    private ApplicationContext context;

    @Override
    public Shipment runProcess(Context ctx, Shipment input) {

        MybatisMapper mapper = (MybatisMapper) context.getBean("mybatisMapper");

        String methodName =  ctx.getMethod();

        if(methodName.isBlank())
        {
            throw new CustomizeException("Method is not Specified");
        }

        try {
            Method method = this.getClass().getMethod(methodName, Shipment.class, MybatisMapper.class);
            Shipment shipment = (Shipment) method.invoke(this, input, mapper);
            return shipment;
        } catch (Exception e) {
            Throwable cause = e.getCause();
            cause.printStackTrace();
            throw new CustomizeException("Error invoking method: " + cause.getMessage());
        }
    }

    public Shipment get (Shipment shipment, MybatisMapper mapper)
    {
        return (Shipment) mapper.runQuery("Shipment","findById", shipment);
    }

    public Shipment insert (Shipment shipment, MybatisMapper mapper)
    {
        mapper.runQuery("Shipment", "insert", shipment);

        return null;
    }

    public Shipment update (Shipment shipment, MybatisMapper mapper)
    {
        mapper.runQuery("Shipment", "update", shipment);

        return null;
    }
}