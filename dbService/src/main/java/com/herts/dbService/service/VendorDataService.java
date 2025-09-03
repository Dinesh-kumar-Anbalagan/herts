package com.herts.dbService.service;

import com.herts.dbService.mybatis.MybatisMapper;
import com.herts.model.*;
import com.herts.orchestrator.exception.CustomizeException;
import com.herts.orchestrator.service.AbstractService;
import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Service("VendorDataService")
public class VendorDataService implements AbstractService<Vendor, InputOutputParams> {
    @Autowired
    private ApplicationContext context;

    @Override
    public ListValues runProcess(Context ctx, Vendor input) {

        MybatisMapper mapper = (MybatisMapper) context.getBean("mybatisMapper");

        String methodName =  ctx.getMethod();

        if(methodName.isBlank())
        {
            throw new CustomizeException("Method is not Specified");
        }

        try {
            Method method = this.getClass().getMethod(methodName, Vendor.class, MybatisMapper.class);
            ListValues vendorList = (ListValues) method.invoke(this, input, mapper);
            return vendorList;
        } catch (Exception e) {
            Throwable cause = e.getCause();
            cause.printStackTrace(); // <- this will show the actual DB/MyBatis error
            throw new CustomizeException("Error invoking method: " + cause.getMessage());
        }
    }

    public ListValues findById (Vendor vendor, MybatisMapper mapper)
    {
        Vendor vendorDb = (Vendor) mapper.runQuery("Vendor","findById", vendor);
        ListValues list = new ListValues();
        list.setVendorList(List.of(vendorDb));
        return list;
    }

    public ListValues retrieve (Vendor vendor, MybatisMapper mapper)
    {
        ListValues list = (ListValues) mapper.runQuery("Vendor","getData", vendor);
        return list;
    }
}