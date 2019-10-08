package org.measure.platform.core.measurement.impl;

import java.net.InetAddress;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticConnection {
    private TransportClient client;
    
    @Value("${measureplatform.elasticsearch.cluster-key}")
	private String clusterKey;
    @Value("${measureplatform.elasticsearch.cluster-name}")
	private String clusterName;
    @Value("${measureplatform.elasticsearch.node.url}")
	private String elasticsearchNodeUrl;
    @Value("${measureplatform.elasticsearch.node.port}")
	private int elasticsearchNodePort;

    @PostConstruct
    public void initIt() throws Exception {
        Settings settings = Settings.builder() .put(clusterKey, clusterName).build();
        this.client = new PreBuiltTransportClient(settings).addTransportAddress(new TransportAddress(InetAddress.getByName(elasticsearchNodeUrl), elasticsearchNodePort));
    }

    @PreDestroy
    public void cleanUp() throws Exception {
        this.client.close();
    }

    public synchronized TransportClient getClient() {
        return client;
    }

}
