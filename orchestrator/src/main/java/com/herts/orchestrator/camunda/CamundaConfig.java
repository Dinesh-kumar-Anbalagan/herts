package com.herts.orchestrator.camunda;

import jakarta.annotation.PostConstruct;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamundaConfig {

    @Bean
    public ProcessEngine processEngine() {

        return ProcessEngineConfiguration
                .createStandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:postgresql://localhost:5432/UK_SHIPMENT?currentSchema=herts_shipment")
                .setJdbcDriver("org.postgresql.Driver")
                .setJdbcUsername("postgres")
                .setJdbcPassword("Mea@1837")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE)
                .setJobExecutorActivate(true)
                .buildProcessEngine();
    }

    @PostConstruct
    public void init() {
        System.out.println("✅ Camunda 7 Engine initialized with Spring Boot 3.");
    }
}