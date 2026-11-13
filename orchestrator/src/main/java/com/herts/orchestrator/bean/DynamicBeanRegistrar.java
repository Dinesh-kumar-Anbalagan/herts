package com.herts.orchestrator.bean;

import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DynamicBeanRegistrar {

    private final GenericApplicationContext context;

    public DynamicBeanRegistrar(GenericApplicationContext context) {
        this.context = context;
    }

    public void registerByClassName(String className, String beanName) {
        try {
            Class<?> clazz = Class.forName(className);
            context.registerBean(beanName, clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
