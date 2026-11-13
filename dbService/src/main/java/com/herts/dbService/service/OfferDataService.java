package com.herts.dbService.service;

import com.herts.dbService.mybatis.MybatisMapper;
import com.herts.model.Context;
import com.herts.model.Offer;
import com.herts.orchestrator.exception.CustomizeException;
import com.herts.orchestrator.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import java.lang.reflect.Method;

@Service("OfferDataService")
public class OfferDataService implements AbstractService<Offer, Offer> {
    @Autowired
    private ApplicationContext context;

    @Override
    public Offer runProcess(Context ctx, Offer input) {
        MybatisMapper mapper = (MybatisMapper) context.getBean("mybatisMapper");

        String methodName =  ctx.getMethod();

        if(methodName.isBlank())
        {
            throw new CustomizeException("Method is not Specified");
        }

        try {
            Method method = this.getClass().getMethod(methodName, Offer.class, MybatisMapper.class);
            Offer offer = (Offer) method.invoke(this, input, mapper);
            return offer;
        } catch (Exception e) {
            Throwable cause = e.getCause();
            cause.printStackTrace();
            throw new CustomizeException("Error invoking method: " + cause.getMessage());
        }
    }

    public Offer creation(Offer offer, MybatisMapper mapper){
        mapper.runQuery("Offer", "insert", offer);
        return offer;
    }

    public Offer updating(Offer offer, MybatisMapper mapper){
        return (Offer) mapper.runQuery("Offer","update", offer);
    }

    public Offer get (Offer offer, MybatisMapper mapper){
        return (Offer) mapper.runQuery("Offer","findById", offer);
    }

    public Offer getDataId (Offer offer, MybatisMapper mapper){
        return (Offer) mapper.runQuery("Offer","getDataId", offer);
    }

    public Offer delete (Offer offer, MybatisMapper mapper){
        return  (Offer) mapper.runQuery("Offer","delete", offer);
    }
}
