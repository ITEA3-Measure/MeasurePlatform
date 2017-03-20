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
import org.measure.platform.measurementstorage.api.IMeasurementStorage;
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

	@Override
	public void putMeasurement(String measureInstance, Boolean manageLast, IMeasurement measurement) {
				
		TransportClient client = connection.getClient();
		client.prepareIndex("measure", measureInstance).setSource(measurement.getValues()).get();
		client.prepareIndex("measure", measureInstance + "-last", "last").setSource(measurement.getValues()).get();		
		
		log.debug("putMeasurement[" + measureInstance + "]: " + measurement.getValues() + " (" +new Date()+")");
	}

	@Override
	public IMeasurement getLastMeasurement(String measureInstance) {
		TransportClient client = connection.getClient();
		SearchResponse response = client.prepareSearch("measure").setTypes(measureInstance + "-last").setIndices("last")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setSize(1).get();

		SearchHit[] results = response.getHits().getHits();
		for (SearchHit hit : results) {
			String sourceAsString = hit.getSourceAsString();
			
			IMeasurement measurement = new DefaultMeasurement();
			try {
				
				Map<String,Object> map = new ObjectMapper().readValue(sourceAsString, new TypeReference<Map<String, Object>>() {});
 				for(Entry<String,Object> entry : map.entrySet()){
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

		SearchResponse response = null;

		if (filter != null && !filter.equals("")) {
			QueryStringQueryBuilder qb = QueryBuilders.queryStringQuery(filter);
			try{
				response = client.prepareSearch("measure").setTypes(measureInstance).setQuery(qb)
						.addSort("postDate", SortOrder.DESC).setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
						.setSize(numberRef).get();
			}catch(Exception e){
				e.printStackTrace();
			}
		} else {
			response = client.prepareSearch("measure").setTypes(measureInstance).addSort("postDate", SortOrder.DESC)
					.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setSize(numberRef).get();
		}

		SearchHit[] results = response.getHits().getHits();
		for (SearchHit hit : results) {
			String sourceAsString = hit.getSourceAsString();
			try {
				IMeasurement measurement = new DefaultMeasurement();
				Map<String,Object> map = new ObjectMapper().readValue(sourceAsString, new TypeReference<Map<String, Object>>() {});
 				for(Entry<String,Object> entry : map.entrySet()){
 					measurement.getValues().put(entry.getKey(), entry.getValue());
				}
				measurements.add(measurement);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return measurements;
	}

}
