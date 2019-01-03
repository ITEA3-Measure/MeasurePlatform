package org.measure.platform.service.measurement.impl;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
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
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class ElasticsearchIndexManager implements IElasticsearchIndexManager {

	private static final String BASE_INDEX = "measure.*";
	private static final String PREFIX_INDEX = "measure.";
	
	@Value("${measure.kibana.adress}")
    private String kibanaAddress;
	
	@Value("${measure.kibana.api}")
	private String kibanaApi;

	@Value("${measure.kibana.version}")
	private String kibanaVersion;

	@Inject
	private ElasticConnection connection;
	
    @Inject
	private IMeasureCatalogueService measureCatalogue;
    
    @Inject
    private MeasureInstanceService measureInstanceService;
    
    @Override
	public void createIndexWithMapping(MeasureInstance measureInstance) {
		TransportClient client = connection.getClient();
		
		final String indexName = PREFIX_INDEX + measureInstance.getInstanceName();
		SMMMeasure measureDefinition = measureCatalogue.getMeasure(measureInstance.getMeasureName());
		
		// Create Measure Index
		createESIndex(measureDefinition, indexName, client);
		
		// Create Kibana Index
		createKibanaIndexPattern(measureInstance);
	}
	
	private void createESIndex(SMMMeasure measureDefinition, final String indexName, TransportClient client) {
		final IndicesExistsResponse res = client.admin().indices().prepareExists(indexName).execute().actionGet();
		if (!res.isExists()) {
			final CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);
			CreateIndexRequest request = new CreateIndexRequest(indexName);
			Map<String, Object> initData = new HashMap<>();
			
			// ADD MAPPING
			for (MeasureUnitField field : measureDefinition.getUnit().getFields()) {
				String fieldName = field.getFieldName();
				String fieldType = field.getFieldType().name().replaceFirst("u_", "");
				
				initData.put(fieldName, field.getFieldType().getInstance());
				XContentBuilder mapping;
				try {
					mapping = jsonBuilder().startObject().startObject("properties").startObject(fieldName).field("type", fieldType).field("index", "true").endObject().endObject().endObject();
					request.mapping(fieldName, mapping);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			// Create the index
			createIndexRequestBuilder.execute().actionGet();
			
			// Push fake data
			client.prepareIndex(indexName, "initialisation").setSource(initData).get();
		}
	}
	
	public void createKibanaIndexPattern(MeasureInstance measureInstance) {
		String indexName = PREFIX_INDEX + measureInstance.getInstanceName();
		
		// Add kibana Index
		RestTemplate addIndexRest = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("kbn-xsrf", "reporting");
		
		MultiValueMap<String, Object> attributes = new LinkedMultiValueMap<String, Object>();
		Map<String, String> values = new HashMap<String, String>();
		values.put("title", indexName);
		attributes.add("attributes", values);
		
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(attributes, headers);

		addIndexRest.postForEntity( "http://" + kibanaAddress + "/api/saved_objects/index-pattern/" + indexName, request , String.class );
	}
	
	public String getKibanaIndexPattern(MeasureInstance measureInstance) {
		String indexName = PREFIX_INDEX + measureInstance.getInstanceName();
		
		// Retrieve kibana Index
		RestTemplate retieveIndexRest = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.add("kbn-xsrf", "reporting");
		
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(headers);

		HttpEntity<String> response = retieveIndexRest.exchange( "http://" + kibanaAddress + "/api/saved_objects/index-pattern/" + indexName, HttpMethod.GET, request, String.class );
		return response.getBody();
	}
	
	@Override
	public void deleteIndex(MeasureInstance measureInstance) {
		TransportClient client = connection.getClient();
		
		// Delete kibana index
		String indexName = PREFIX_INDEX + measureInstance.getInstanceName();
		final IndicesExistsResponse result = client.admin().indices().prepareExists(indexName).execute().actionGet();
		if (result.isExists()) {
			RestTemplate deleteIndexRest = new RestTemplate();
			
			HttpHeaders headers = new HttpHeaders();
			headers.add("kbn-xsrf", "reporting");
			
			HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(headers);

			deleteIndexRest.exchange( "http://" + kibanaAddress + "/api/saved_objects/index-pattern/" + indexName, HttpMethod.DELETE, request, String.class );
		}
		
		// Delete the index if exist
		final IndicesExistsResponse res = client.admin().indices().prepareExists(indexName).execute().actionGet();
		if (res.isExists()) {
			final DeleteIndexRequestBuilder delIdx = client.admin().indices().prepareDelete(indexName);
			delIdx.execute().actionGet();
		}
	}
	
	@Override
	public void updateIndex(List<SMMMeasure> measures) {
		// Update Base Kibana Indices if required
		TransportClient client = connection.getClient();
		for (SMMMeasure measureDefinition : measures) {
			List<MeasureInstance> measureInstances = measureInstanceService.findMeasureInstanceByReference(measureDefinition.getName());
			for (MeasureInstance instance : measureInstances) {
				// Create Elasticsearch index if required
				final String indexName = PREFIX_INDEX + instance.getInstanceName();
				final IndicesExistsResponse res = client.admin().indices().prepareExists(indexName).execute().actionGet();
				if (!res.isExists()) {
					createESIndex(measureDefinition, indexName, client);
				}
				
				// Create kibana Index if required
				if (getKibanaIndexPattern(instance).isEmpty()) {
					createKibanaIndexPattern(instance);
				}
			}
		}
		
		// Create Generic Kibana Index if required 
		if (HttpStatus.NOT_FOUND.equals(getKibanaGenericIndexCodeStatus())) {
			createKibanaGenericIndex();
		}
	}
	
	public void createKibanaGenericIndex() {
		String baseIndex = getBaseMeasureIndex();
		
		// Add kibana Generic Index
		RestTemplate addIndexRest = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("kbn-xsrf", "reporting");
		
		MultiValueMap<String, Object> attributes = new LinkedMultiValueMap<String, Object>();
		Map<String, String> values = new HashMap<String, String>();
		values.put("title", baseIndex);
		attributes.add("attributes", values);
		
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(attributes, headers);

		addIndexRest.postForEntity( "http://" + kibanaAddress + "/api/saved_objects/index-pattern/" + baseIndex, request , String.class );
	}
	
	public HttpStatus getKibanaGenericIndexCodeStatus() {
		String baseIndex = getBaseMeasureIndex();
		
		// Retrieve kibana Index
		RestTemplate retieveIndexRest = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.add("kbn-xsrf", "reporting");
		
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(headers);

		try {
			return retieveIndexRest.exchange( "http://" + kibanaAddress + "/api/saved_objects/index-pattern/" + baseIndex, HttpMethod.GET, request, String.class ).getStatusCode();
		} catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
		    if(HttpStatus.NOT_FOUND.equals(httpClientOrServerExc.getStatusCode())) {
		    	return httpClientOrServerExc.getStatusCode();
		    }
		}
		return null;
	}

	public void deleteKibanaGenericIndex() {
		// Delete kibana Generic index
		String baseIndex = getBaseMeasureIndex();
		RestTemplate deleteIndexRest = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.add("kbn-xsrf", "reporting");

		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(headers);

		deleteIndexRest.exchange("http://" + kibanaAddress + "/api/saved_objects/index-pattern/" + baseIndex,
				HttpMethod.DELETE, request, String.class);
	}

	@Override
	public String getBaseMeasureIndex() {
		return BASE_INDEX;
	}

}
