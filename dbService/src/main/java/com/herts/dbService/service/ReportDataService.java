package com.herts.dbService.service;

import com.herts.dbService.mybatis.MybatisMapper;

import com.herts.model.*;
import com.herts.orchestrator.exception.CustomizeException;
import com.herts.orchestrator.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

@Service("ReportDataService")
public class ReportDataService implements AbstractService<Report, InputOutputParams> {

    @Autowired
    private ApplicationContext context;

    @Override
    public Report runProcess(Context ctx, Report input) {

        MybatisMapper mapper = (MybatisMapper) context.getBean("mybatisMapper");

        String methodName =  ctx.getMethod();

        if(methodName.isBlank())
        {
            throw new CustomizeException("Method is not Specified");
        }

        try {
            Method method = this.getClass().getMethod(methodName, Report.class, MybatisMapper.class);
            Report report = (Report) method.invoke(this, input, mapper);
            return report;
        } catch (Exception e) {
            Throwable cause = e.getCause();
            cause.printStackTrace(); // <- this will show the actual DB/MyBatis error
            throw new CustomizeException("Error invoking method: " + cause.getMessage());
        }
    }

    public Report creation(Report report, MybatisMapper mapper){
        mapper.runQuery("Report", "insert", report);
        return report;
    }

    public Report retrieve(Report report, MybatisMapper mapper) {
        Report reportRes = (Report) mapper.runQuery("Report", "findById", report);
        return reportRes;
    }
}