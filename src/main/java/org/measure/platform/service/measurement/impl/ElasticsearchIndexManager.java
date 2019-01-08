package org.measure.platform.service.measurement.impl;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.measure.platform.core.api.IMeasureCatalogueService;
import org.measure.platform.core.api.entitys.MeasureInstanceService;
import org.measure.platform.core.entity.MeasureInstance;
import org.measure.platform.service.measurement.api.IElasticsearchIndexManager;
import org.measure.smm.measure.model.MeasureUnitField;
import org.measure.smm.measure.model.SMMMeasure;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class ElasticsearchIndexManager implements IElasticsearchIndexManager {

	@Value("${measure.kibana.adress}")
	private String kibanaAddress;

	@Inject
	private ElasticConnection connection;

	@Inject
	private IMeasureCatalogueService measureCatalogue;

	@Override
	public void createIndexWithMapping(MeasureInstance measureInstance) {
		TransportClient client = connection.getClient();

		final String indexName = IndexFormat.PREFIX_INDEX + measureInstance.getInstanceName();
		SMMMeasure measureDefinition = measureCatalogue.getMeasure(measureInstance.getMeasureName());

		// Create Measure Index
		createESIndex(measureDefinition, indexName, client);

		// Create Kibana Index
		createKibanaIndexPattern(measureInstance);
	}

	private void createESIndex(SMMMeasure measureDefinition, final String indexName, TransportClient client) {
		final IndicesExistsResponse res = client.admin().indices().prepareExists(indexName).execute().actionGet();
		if (!res.isExists()) {
			CreateIndexRequest request = new CreateIndexRequest(indexName);
			// ADD MAPPING
			try {
				XContentBuilder mapping = jsonBuilder().startObject().startObject("_doc").startObject("properties");
				for (MeasureUnitField field : measureDefinition.getUnit().getFields()) {
					String fieldName = field.getFieldName();
					String fieldType = field.getFieldType().name().replaceFirst("u_", "");

					mapping.startObject(fieldName).field("type", fieldType).endObject();
				}
				mapping.endObject().endObject().endObject();
				request.mapping("_doc", mapping);
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Create the index
			client.admin().indices().create(request).actionGet();
		}
	}

	private void createKibanaIndexPattern(MeasureInstance measureInstance) {
		String indexName = IndexFormat.PREFIX_INDEX + measureInstance.getInstanceName();

		// Add kibana Index
		RestTemplate addIndexRest = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.add("kbn-xsrf", "reporting");

		MultiValueMap<String, Object> attributes = new LinkedMultiValueMap<String, Object>();
		Map<String, String> values = new HashMap<String, String>();
		values.put("title", indexName);
		values.put("timeFieldName", "postDate");

		attributes.add("attributes", values);

		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(attributes,
				headers);
		addIndexRest.postForEntity("http://" + kibanaAddress + "/api/saved_objects/index-pattern/" + indexName, request,
				String.class);
	}

	@Override
	public void deleteIndex(MeasureInstance measureInstance) {
		// Delete kibana index
		String indexName = IndexFormat.PREFIX_INDEX + measureInstance.getInstanceName();
		if (isKibanaIndexExist(indexName)) {
			RestTemplate deleteIndexRest = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.add("kbn-xsrf", "reporting");

			HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(headers);

			deleteIndexRest.exchange("http://" + kibanaAddress + "/api/saved_objects/index-pattern/" + indexName,
					HttpMethod.DELETE, request, String.class);
		}
		
		// Delete the Elasticsearch index if exist
		TransportClient client = connection.getClient();
		final IndicesExistsResponse res = client.admin().indices().prepareExists(indexName).execute().actionGet();
		if (res.isExists()) {
			final DeleteIndexRequestBuilder delIdx = client.admin().indices().prepareDelete(indexName);
			delIdx.execute().actionGet();
		}
	}

	@Override
	public void updateIndex(List<SMMMeasure> measures) {
		// Create Default Elasticsearch Index
		createESGenericIndex();

		// Create Generic Kibana Index if required
		createKibanaGenericIndex();
	}

	private void createESGenericIndex() {
		TransportClient client = connection.getClient();
		final IndicesExistsResponse res = client.admin().indices().prepareExists(IndexFormat.ES_BASE_INDEX).execute().actionGet();
		if (!res.isExists()) {
			CreateIndexRequest request = new CreateIndexRequest(IndexFormat.ES_BASE_INDEX);
			client.admin().indices().create(request).actionGet();
		}
	}

	private void createKibanaGenericIndex() {
		if (!isKibanaIndexExist(IndexFormat.KIBANA_BASE_INDEX)) {
			RestTemplate kibanaRest = new RestTemplate();

			// Add kibana Generic Index
			HttpHeaders headers = new HttpHeaders();
			headers.add("kbn-xsrf", "reporting");

			MultiValueMap<String, Object> attributes = new LinkedMultiValueMap<String, Object>();
			Map<String, String> values = new HashMap<String, String>();
			values.put("title", IndexFormat.KIBANA_BASE_INDEX);
			attributes.add("attributes", values);

			HttpEntity<MultiValueMap<String, Object>> indexRequest = new HttpEntity<MultiValueMap<String, Object>>(
					attributes, headers);
			kibanaRest.postForEntity("http://" + kibanaAddress + "/api/saved_objects/index-pattern/" + IndexFormat.KIBANA_BASE_INDEX,
					indexRequest, String.class);

			// Set kibana Index as default
			HttpHeaders defaultHeaders = new HttpHeaders();
			defaultHeaders.add("kbn-xsrf", "reporting");

			MultiValueMap<String, String> defaultValue = new LinkedMultiValueMap<String, String>();
			defaultValue.add("value", IndexFormat.KIBANA_BASE_INDEX);
			HttpEntity<MultiValueMap<String, String>> defaultRequest = new HttpEntity<MultiValueMap<String, String>>(
					defaultValue, defaultHeaders);
			kibanaRest.postForEntity("http://localhost:5601/api/kibana/settings/defaultIndex", defaultRequest,
					String.class);
		}
	}

	private boolean isKibanaIndexExist(String index) {
		// Retrieve kibana Index
		RestTemplate retieveIndexRest = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.add("kbn-xsrf", "reporting");

		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(headers);

		try {
			retieveIndexRest.exchange("http://" + kibanaAddress + "/api/saved_objects/index-pattern/" + index,
					HttpMethod.GET, request, String.class).getStatusCode();
		} catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			if (HttpStatus.NOT_FOUND.equals(httpClientOrServerExc.getStatusCode())) {
				return false;
			}
		}
		return true;
	}

}
