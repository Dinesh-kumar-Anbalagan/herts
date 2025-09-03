package com.herts.dbService.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.herts.dbService.mybatis.MybatisMapper;
import com.herts.model.Context;
import com.herts.model.Customer;
import com.herts.orchestrator.service.AbstractService;
import com.herts.orchestrator.exception.CustomizeException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

@Service("CustomerCreationDataService")
public class CustomerCreationDataService implements AbstractService<Customer, Customer> {

    @Autowired
    private ApplicationContext context;


    @Override
    public Customer runProcess(Context ctx, Customer input) {
       MybatisMapper mapper = (MybatisMapper) context.getBean("mybatisMapper");

        String methodName =  ctx.getMethod();

        if(methodName.isBlank())
        {
            throw new CustomizeException("Method is not Specified");
        }

        try {
            Method method = this.getClass().getMethod(methodName, Customer.class, MybatisMapper.class);
            Customer cust = (Customer) method.invoke(this, input, mapper);
            return cust;
        } catch (Exception e) {
            Throwable cause = e.getCause();
            cause.printStackTrace(); // <- this will show the actual DB/MyBatis error
            throw new CustomizeException("Error invoking method: " + cause.getMessage());
        }
    }

    public Customer creation(Customer cust, MybatisMapper mapper){
              mapper.runQuery("Customer", "insert", cust);
              Customer customer =  new Customer();
              customer.setMessage("Inserted");
              return customer;
        }

    public Customer retrieve(Customer cust, MybatisMapper mapper) {
            Customer rescust = (Customer) mapper.runQuery("Customer", "findById", cust);
            return rescust;
    }

    public Customer getDataId(Customer cust, MybatisMapper mapper) {
        Customer rescust = (Customer) mapper.runQuery("Customer", "getDataId", cust);
        return rescust;
    }

    public Customer updating(Customer cust, MybatisMapper mapper){
        return (Customer) mapper.runQuery("Customer","update", cust);
    }
}
