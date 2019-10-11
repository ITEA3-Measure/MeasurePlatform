package org.measure.platform.core.measurement.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.measure.platform.core.measurement.api.IMeasurementStorage;
import org.measure.platform.restapi.measure.dto.KibanaVisualisation;
import org.measure.smm.measure.api.IMeasurement;
import org.measure.smm.measure.defaultimpl.measurements.DefaultMeasurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ElasticMeasurementStorage implements IMeasurementStorage {
	
	@Value("${measureplatform.kibana.address}")
	private String kibanaAddress;
	
	@Inject
	private ElasticConnection connection;
	
	@Override
	public void putMeasurement(String measureInstance, IMeasurement measurement) {
		if (measurement.getValues().get("postDate") == null) {
			measurement.getValues().put("postDate", new Date());
		}
		String indexName = IndexFormat.getMeasureInstanceIndex(measureInstance);
		TransportClient client = connection.getClient();
		client.index(new IndexRequest(indexName, "_doc").source(measurement.getValues()));
	}

	@Override
	public List<IMeasurement> getMeasurement(String measureInstance, Integer numberRef, String filter) {
		List<IMeasurement> measurements = new ArrayList<>();
		TransportClient client = connection.getClient();
		String indexName = IndexFormat.getMeasureInstanceIndex(measureInstance);

		SearchResponse response = null;

		if (filter != null && !filter.equals("")) {
			QueryStringQueryBuilder qb = QueryBuilders.queryStringQuery(filter);
			try {
				response = client.prepareSearch(indexName).setTypes("_doc").setQuery(qb).addSort("postDate", SortOrder.DESC).setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setSize(numberRef).get();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			response = client.prepareSearch(indexName).setTypes("_doc").addSort("postDate", SortOrder.DESC).setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setSize(numberRef).get();
		}

		SearchHit[] results = response.getHits().getHits();
		for (SearchHit hit : results) {
			String sourceAsString = hit.getSourceAsString();
			try {
				IMeasurement measurement = new DefaultMeasurement();
				Map<String, Object> map = new ObjectMapper().readValue(sourceAsString, new TypeReference<Map<String, Object>>() {
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
	public List<IMeasurement> getMeasurementPage(String measureInstance, Integer size, Integer page, String filter) {
		List<IMeasurement> measurements = new ArrayList<>();
		TransportClient client = connection.getClient();
		String indexName = IndexFormat.getMeasureInstanceIndex(measureInstance);

		SearchResponse scrollResp = null;

		if (filter != null && !filter.equals("")) {
			QueryStringQueryBuilder qb = QueryBuilders.queryStringQuery(filter);
			scrollResp = client.prepareSearch(indexName).setTypes("_doc").addSort("postDate", SortOrder.DESC).setScroll(new TimeValue(60000)).setQuery(qb).setSize(size).execute().actionGet();
		} else {
			scrollResp = client.prepareSearch(indexName).setTypes("_doc").addSort("postDate", SortOrder.DESC).setScroll(new TimeValue(60000)).setSize(size).execute().actionGet();
		}

		// Scroll until no hits are returned

		int pageCount = 1;
		while (true) {
			// Break condition: No hits are returned
			if (scrollResp.getHits().getHits().length == 0) {
				break;
			} else if (pageCount >= page) {
				break;
			}
			pageCount++;
			scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
		}

		SearchHit[] results = scrollResp.getHits().getHits();
		for (SearchHit hit : results) {
			String sourceAsString = hit.getSourceAsString();
			try {
				IMeasurement measurement = new DefaultMeasurement();
				Map<String, Object> map = new ObjectMapper().readValue(sourceAsString, new TypeReference<Map<String, Object>>() {
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
		
		RestTemplate findVisualisationsRest = new RestTemplate();
		
		int _pageSize = 1000;
		
		ResponseEntity<String> response = findVisualisationsRest.getForEntity("http://" + kibanaAddress + "/api/saved_objects/_find?type=visualization&search_fields=id,title?fields=id,title&per_page=" + _pageSize, String.class);
		try {
			JSONObject jObject = new JSONObject(response.getBody());
			JSONArray jArray = jObject.getJSONArray("saved_objects");
		
			for (int i = 0 ; i < jArray.length(); i++) {
				JSONObject obj = jArray.getJSONObject(i);
				KibanaVisualisation kibanaVisualisation = new KibanaVisualisation();
				kibanaVisualisation.setId(obj.getString("id"));
				kibanaVisualisation.setName(obj.getJSONObject("attributes").getString("title"));
				results.add(kibanaVisualisation);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return results;
	}
	
	@Override
	public List<KibanaVisualisation> findKibanaDashboard() {
		List<KibanaVisualisation> results = new ArrayList<>();
		RestTemplate findVisualisationsRest = new RestTemplate();

		int _pageSize = 1000;

		ResponseEntity<String> response = findVisualisationsRest.getForEntity("http://" + kibanaAddress	+ "/api/saved_objects/_find?type=dashboard&search_fields=id,title?fields=id,title&per_page=" + _pageSize, String.class);
		try {
			JSONObject jObject = new JSONObject(response.getBody());
			JSONArray jArray = jObject.getJSONArray("saved_objects");
	
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject obj = jArray.getJSONObject(i);
				KibanaVisualisation kibanaVisualisation = new KibanaVisualisation();
				kibanaVisualisation.setId(obj.getString("id"));
				kibanaVisualisation.setName(obj.getJSONObject("attributes").getString("title"));
				results.add(kibanaVisualisation);
			}
		
		} catch (JSONException e) {	
			e.printStackTrace();
		}
		return results;
	}

	@Override
	public IMeasurement getLastMeasurement(String measureInstance) {
		TransportClient client = connection.getClient();
		String indexName = IndexFormat.getMeasureInstanceIndex(measureInstance);

		SearchResponse response  = client.prepareSearch(indexName).setTypes("_doc").addSort("postDate", SortOrder.DESC).setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setSize(1).get();
		

		SearchHit[] results = response.getHits().getHits();
		for (SearchHit hit : results) {
			String sourceAsString = hit.getSourceAsString();
			try {
				IMeasurement measurement = new DefaultMeasurement();
				Map<String, Object> map = new ObjectMapper().readValue(sourceAsString, new TypeReference<Map<String, Object>>() {
				});
				for (Entry<String, Object> entry : map.entrySet()) {
					measurement.getValues().put(entry.getKey(), entry.getValue());
				}
				return measurement;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}

}
