package org.measure.platform.core.measurement.impl;

import java.net.InetAddress;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticConnection {
    private TransportClient client;

    @PostConstruct
    public void initIt() throws Exception {
        Settings settings = Settings.builder() .put("cluster.name", "elasticsearch").build();
        this.client = new PreBuiltTransportClient(settings).addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
    }

    @PreDestroy
    public void cleanUp() throws Exception {
        this.client.close();
    }

    public synchronized TransportClient getClient() {
        return client;
    }

}
