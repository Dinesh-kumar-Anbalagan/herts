package com.herts.esData.configure;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Component;

@Component
public class ConfigureElasticsearchClient {

    public static ElasticsearchClient createClient() {
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200)).build();

        RestClientTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        return new ElasticsearchClient(transport);
    }
}

