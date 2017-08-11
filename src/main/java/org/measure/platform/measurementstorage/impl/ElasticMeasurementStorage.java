package org.measure.platform.measurementstorage.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.measure.platform.measurementstorage.api.IMeasurementStorage;
import org.measure.platform.restapi.app.services.dto.KibanaVisualisation;
import org.measure.smm.measure.api.IMeasurement;
import org.measure.smm.measure.defaultimpl.measurements.DefaultMeasurement;
import org.measure.smm.measure.model.MeasureUnitField;
import org.measure.smm.measure.model.SMMMeasure;
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

		measurement.getValues().put("postDate", new Date());

		TransportClient client = connection.getClient();
		client.prepareIndex("measure", measureInstance).setSource(measurement.getValues()).get();
		client.prepareIndex("measure", measureInstance + "-last", "last").setSource(measurement.getValues()).get();

		log.debug("putMeasurement[" + measureInstance + "]: " + measurement.getValues() + " (" + new Date() + ")");
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

		SearchResponse response = null;

		if (filter != null && !filter.equals("")) {
			QueryStringQueryBuilder qb = QueryBuilders.queryStringQuery(filter);
			try {
				response = client.prepareSearch("measure").setTypes(measureInstance).setQuery(qb)
						.addSort("postDate", SortOrder.DESC).setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
						.setSize(numberRef).get();
			} catch (Exception e) {
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
	public void createMeasureIndex(SMMMeasure measureDefinition, String measureInstanceName) {
		TransportClient client = connection.getClient();
		for (MeasureUnitField field : measureDefinition.getUnit().getFields()) {

			String fieldName = field.getFieldName();
			String fieldType = field.getFieldType().name().replaceFirst("u_", "");

			client.admin().indices().preparePutMapping("measure").setType(fieldName)
					.setSource("{\n" + "  \"properties\": {\n" + "    \"" + fieldName + "\": {\n" + "      \"type\": \""
							+ fieldType + "\",\n" + "      \"index\": \"true\"\n" + "    }\n" + "  }\n" + "}")
					.get();
		}

		// Update Index
		client.admin().indices().prepareRefresh("measure").get();

	}

	@Override
	public List<KibanaVisualisation> findKibanaVisualisation() {
		List<KibanaVisualisation> results = new ArrayList<>();
		TransportClient client = connection.getClient();
		SearchResponse visResponse = client.prepareSearch(".kibana").setTypes("visualization")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).get();
		SearchResponse urlResponse = client.prepareSearch(".kibana").setTypes("url")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).get();

		for (SearchHit visHit : visResponse.getHits().getHits()) {
			String title = (String) visHit.getSource().get("title");
			String id = (String) visHit.getId();

			for (SearchHit urlHit : urlResponse.getHits().getHits()) {
				String url = (String) urlHit.getSource().get("url");

				if (url.contains(id)) {
					KibanaVisualisation visualisation = new KibanaVisualisation();
					visualisation.setName(title);
					visualisation.setUrl(url);

					results.add(visualisation);
					break;
				}

			}

		}
		return results;
	}
	
	

	@Override
	public KibanaVisualisation findKibanaVisualisationByName(String name) {
		List<KibanaVisualisation> results = new ArrayList<>();
		TransportClient client = connection.getClient();
		SearchResponse visResponse = client.prepareSearch(".kibana").setTypes("visualization")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).get();
		SearchResponse urlResponse = client.prepareSearch(".kibana").setTypes("url")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).get();

		for (SearchHit visHit : visResponse.getHits().getHits()) {
			String title = (String) visHit.getSource().get("title");
			String id = (String) visHit.getId();
			if (name.equals(title)) {

				for (SearchHit urlHit : urlResponse.getHits().getHits()) {
					String url = (String) urlHit.getSource().get("url");

					if (url.contains(id)) {
						KibanaVisualisation visualisation = new KibanaVisualisation();
						visualisation.setName(title);
						visualisation.setUrl(url);

						return visualisation;
					}

				}
			}
		}
		return null;
	}
	
	@Override
	public List<KibanaVisualisation> findKibanaDashboard() {
		List<KibanaVisualisation> results = new ArrayList<>();
		TransportClient client = connection.getClient();

		SearchResponse dashResponse = client.prepareSearch(".kibana").setTypes("dashboard")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).get();
		SearchResponse urlResponse = client.prepareSearch(".kibana").setTypes("url")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).get();


		for (SearchHit visHit : dashResponse.getHits().getHits()) {
			String title = (String) visHit.getSource().get("title");
			String id = (String) visHit.getId();

			for (SearchHit urlHit : urlResponse.getHits().getHits()) {
				String url = (String) urlHit.getSource().get("url");

				if (url.contains(id)) {
					KibanaVisualisation visualisation = new KibanaVisualisation();
					visualisation.setName(title);
					visualisation.setUrl(url);

					results.add(visualisation);
					break;
				}

			}

		}
		return results;
	}
	
	@Override
	public KibanaVisualisation findKibanaDashboardByName(String name) {
		List<KibanaVisualisation> results = new ArrayList<>();
		TransportClient client = connection.getClient();
		SearchResponse dashResponse = client.prepareSearch(".kibana").setTypes("dashboard")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).get();
		SearchResponse urlResponse = client.prepareSearch(".kibana").setTypes("url")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).get();


		
		for (SearchHit visHit : dashResponse.getHits().getHits()) {
			String title = (String) visHit.getSource().get("title");
			String id = (String) visHit.getId();
			if (name.equals(title)) {

				for (SearchHit urlHit : urlResponse.getHits().getHits()) {
					String url = (String) urlHit.getSource().get("url");

					if (url.contains(id)) {
						KibanaVisualisation visualisation = new KibanaVisualisation();
						visualisation.setName(title);
						visualisation.setUrl(url);

						return visualisation;
					}

				}
			}
		}
		return null;
	}

}
