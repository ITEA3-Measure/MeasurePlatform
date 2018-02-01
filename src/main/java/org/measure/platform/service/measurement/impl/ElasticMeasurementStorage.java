package org.measure.platform.measurementstorage.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.measure.platform.measurementstorage.api.IElasticsearchIndexManager;
import org.measure.platform.measurementstorage.api.IMeasurementStorage;
import org.measure.platform.restapi.app.services.dto.KibanaVisualisation;
import org.measure.smm.measure.api.IMeasurement;
import org.measure.smm.measure.defaultimpl.measurements.DefaultMeasurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ElasticMeasurementStorage implements IMeasurementStorage {
    private final Logger log = LoggerFactory.getLogger(ElasticMeasurementStorage.class);

    @Inject
    private ElasticConnection connection;

    @Inject
    private IElasticsearchIndexManager indexManager;

    @Override
    public void putMeasurement(String index, String measureInstance, Boolean manageLast, IMeasurement measurement) {
        if(measurement.getValues().get("postDate") == null){
            measurement.getValues().put("postDate", new Date());
        }
        
        TransportClient client = connection.getClient();
        client.prepareIndex(index, measureInstance).setSource(measurement.getValues()).get();
        client.prepareIndex(index, measureInstance + "-last", "last").setSource(measurement.getValues()).get();
        
        log.debug("putMeasurement[" + measureInstance + "]: " + measurement.getValues() + " (" + new Date() + ")");
    }

    @Override
    public IMeasurement getLastMeasurement(String measureInstance) {
        TransportClient client = connection.getClient();
        String baseIndex = indexManager.getBaseMeasureIndex();
        SearchResponse response = client.prepareSearch(baseIndex).setTypes(measureInstance + "-last").setIndices("last")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setSize(1).get();
        
        SearchHit[] results = response.getHits().getHits();
        for (SearchHit hit : results) {
            String sourceAsString = hit.getSourceAsString();
        
            IMeasurement measurement = new DefaultMeasurement();
            try {
        
                Map<String, Object> map = new ObjectMapper().readValue(sourceAsString,
                        new TypeReference<Map<String, Object>>() {
                        });
                for (Entry<String, Object> entry : map.entrySet()) {
                    measurement.getValues().put(entry.getKey(), entry.getValue());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        
            return measurement;
        }
        return null;
    }

    @Override
    public List<IMeasurement> getMeasurement(String measureInstance, Integer numberRef, String filter) {
        List<IMeasurement> measurements = new ArrayList<>();
        TransportClient client = connection.getClient();
        String baseIndex = indexManager.getBaseMeasureIndex();
        
        SearchResponse response = null;
        
        if (filter != null && !filter.equals("")) {
            QueryStringQueryBuilder qb = QueryBuilders.queryStringQuery(filter);
            try {
                response = client.prepareSearch(baseIndex).setTypes(measureInstance).setQuery(qb)
                        .addSort("postDate", SortOrder.DESC).setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                        .setSize(numberRef).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            response = client.prepareSearch(baseIndex).setTypes(measureInstance).addSort("postDate", SortOrder.DESC)
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setSize(numberRef).get();
        }
        
        SearchHit[] results = response.getHits().getHits();
        for (SearchHit hit : results) {
            String sourceAsString = hit.getSourceAsString();
            try {
                IMeasurement measurement = new DefaultMeasurement();
                Map<String, Object> map = new ObjectMapper().readValue(sourceAsString,
                        new TypeReference<Map<String, Object>>() {
                        });
                for (Entry<String, Object> entry : map.entrySet()) {
                    measurement.getValues().put(entry.getKey(), entry.getValue());
                }
                measurements.add(measurement);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return measurements;
    }

    @Override
    public List<KibanaVisualisation> findKibanaVisualisation() {
        List<KibanaVisualisation> results = new ArrayList<>();
        TransportClient client = connection.getClient();
        SearchResponse visResponse = client.prepareSearch(".kibana").setTypes("visualization")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH).get();
        
        for (SearchHit visHit : visResponse.getHits().getHits()) {            
            KibanaVisualisation visualisation = new KibanaVisualisation();
            visualisation.setName((String) visHit.getSource().get("title"));
            visualisation.setId((String) visHit.getId());
            results.add(visualisation);
        }
        return results;
    }

    @Override
    public List<KibanaVisualisation> findKibanaDashboard() {
        List<KibanaVisualisation> results = new ArrayList<>();
        TransportClient client = connection.getClient();
        
        SearchResponse dashResponse = client.prepareSearch(".kibana").setTypes("dashboard")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH).get();
        
        
        for (SearchHit visHit : dashResponse.getHits().getHits()) {
            KibanaVisualisation visualisation = new KibanaVisualisation();
            visualisation.setName( (String) visHit.getSource().get("title"));
            visualisation.setId((String) visHit.getId());
            results.add(visualisation);
        }
        return results;
    }

}
