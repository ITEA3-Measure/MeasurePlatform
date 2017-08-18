package org.measure.platform.measurementstorage.impl;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.measure.platform.measurementstorage.api.IElasticsearchIndexManager;
import org.measure.smm.measure.model.MeasureUnitField;
import org.measure.smm.measure.model.SMMMeasure;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ElasticsearchIndexManager implements IElasticsearchIndexManager {

	private static final String BASE_INDEX = "measure";
	@Inject
	private ElasticConnection connection;
	
	@Value("${measure.kibana.api}")
	private String kibanaApi;



	@Override
	public void createIndexWithMapping(SMMMeasure measureDefinition) {
		final String indexName = measureDefinition.getName().toLowerCase();

		TransportClient client = connection.getClient();

		final CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);

		// ADD MAPPING

		for (MeasureUnitField field : measureDefinition.getUnit().getFields()) {

			String fieldName = field.getFieldName();
			String fieldType = field.getFieldType().name().replaceFirst("u_", "");

			XContentBuilder mapping;
			try {
				mapping = jsonBuilder().startObject().startObject("properties").startObject(fieldName)
						.field("type", fieldType).field("index", "true").endObject().endObject().endObject();

				createIndexRequestBuilder.addMapping(fieldName, mapping);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// MAPPING DONE
		createIndexRequestBuilder.execute().actionGet();

		client.admin().indices().prepareAliases().addAlias(indexName, BASE_INDEX).get();

		RestTemplate restTemplate = new RestTemplate();
		try {
			Map<String,String> values = new HashMap<>();
			values.put("title", "measure");
			values.put("timeFieldName", "postDate");
			
			restTemplate.put(
					"http://"+kibanaApi+"/.kibana/index-pattern/measure",
					values);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	@Override
	public void deleteIndex(SMMMeasure measureDefinition) {
		TransportClient client = connection.getClient();

		// Create Index and set settings and mappings
		final String indexName = measureDefinition.getName().toLowerCase();

		// Delete the index if exist
		final IndicesExistsResponse res = client.admin().indices().prepareExists(indexName).execute().actionGet();
		if (res.isExists()) {
			final DeleteIndexRequestBuilder delIdx = client.admin().indices().prepareDelete(indexName);
			delIdx.execute().actionGet();

		}
	}

}
