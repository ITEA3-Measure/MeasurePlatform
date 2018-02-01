package org.measure.platform.service.measurement.impl;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.elasticsearch.action.admin.indices.alias.exists.AliasesExistResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.measure.platform.service.measurement.api.IElasticsearchIndexManager;
import org.measure.smm.measure.model.MeasureUnitField;
import org.measure.smm.measure.model.SMMMeasure;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ElasticsearchIndexManager implements IElasticsearchIndexManager {
	private static final String BASE_INDEX = "all-measure";

	@Value("${measure.kibana.api}")
	private String kibanaApi;

	@Value("${measure.kibana.version}")
	private String kibanaVersion;

	@Inject
	private ElasticConnection connection;

	@Override
	public void createIndexWithMapping(SMMMeasure measureDefinition) {
		final String indexName = measureDefinition.getName().toLowerCase();
		TransportClient client = connection.getClient();

		// Create Measure Index
		createIndex(measureDefinition, indexName, client);

		// Create Kibana Index
		createAlias(indexName, client);
	}

	private void createIndex(SMMMeasure measureDefinition, final String indexName, TransportClient client) {
		final IndicesExistsResponse res = client.admin().indices().prepareExists(indexName).execute().actionGet();
		if (!res.isExists()) {
			final CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);

			Map<String, Object> initData = new HashMap<>();

			// ADD MAPPING
			for (MeasureUnitField field : measureDefinition.getUnit().getFields()) {
				String fieldName = field.getFieldName();
				String fieldType = field.getFieldType().name().replaceFirst("u_", "");
				initData.put(fieldName, field.getFieldType().getInstance());
				XContentBuilder mapping;
				try {
					mapping = jsonBuilder().startObject().startObject("properties").startObject(fieldName).field("type", fieldType).field("index", "true").endObject().endObject().endObject();
					createIndexRequestBuilder.addMapping(fieldName, mapping);
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

	private void createAlias(final String indexName, TransportClient client) {
		// create kibana alias 'BASE_INDEX' which mearge all index
		client.admin().indices().prepareAliases().addAlias(indexName, BASE_INDEX).get();

		// set base index as default kibana index;
		RestTemplate addMergedIndexRest = new RestTemplate();
		try {
			Map<String, String> values = new HashMap<>();
			values.put("title", BASE_INDEX);
			values.put("timeFieldName", "postDate");

			addMergedIndexRest.put("http://" + kibanaApi + "/.kibana/index-pattern/" + BASE_INDEX, values);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// // set this index as default index
		RestTemplate defaultindexRest = new RestTemplate();
		try {
			Map<String, String> values = new HashMap<>();
			values.put("defaultIndex", BASE_INDEX);
			defaultindexRest.put("http://" + kibanaApi + "/.kibana/config/" + kibanaVersion, values);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// create specific kibana alias for this measure
		client.admin().indices().prepareAliases().addAlias(indexName, indexName + "-alias").get();

		// set base index as default kibana index;
		RestTemplate addIndexRest = new RestTemplate();
		try {
			Map<String, String> values = new HashMap<>();
			values.put("title", indexName + "-alias");
			values.put("timeFieldName", "postDate");

			addIndexRest.put("http://" + kibanaApi + "/.kibana/index-pattern/" + indexName + "-alias", values);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteIndex(SMMMeasure measureDefinition) {
		TransportClient client = connection.getClient();

		// Create Index and set settings and mappings
		final String indexName = measureDefinition.getName().toLowerCase();

		// Remove specific kibana alias for this measure
		final AliasesExistResponse alias = client.admin().indices().prepareAliasesExist(indexName + "-alias").execute().actionGet();
		if (alias.isExists()) {
			client.admin().indices().prepareAliases().removeAlias(indexName, indexName + "-alias").execute().actionGet();
			client.admin().indices().prepareAliases().removeAlias(indexName, BASE_INDEX).execute().actionGet();

			// Delete index pattern
			RestTemplate addIndexRest = new RestTemplate();
			try {
				addIndexRest.delete("http://" + kibanaApi + "/.kibana/index-pattern/" + indexName + "-alias");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Delete the index if exist
		final IndicesExistsResponse res = client.admin().indices().prepareExists(indexName).execute().actionGet();
		if (res.isExists()) {
			final DeleteIndexRequestBuilder delIdx = client.admin().indices().prepareDelete(indexName);
			delIdx.execute().actionGet();
		}
	}

	@Override
	public String getBaseMeasureIndex() {
		return BASE_INDEX;
	}

	@Override
	public void updateIndex(List<SMMMeasure> measures) {
		TransportClient client = connection.getClient();
		for (SMMMeasure measureDefinition : measures) {
			final String indexName = measureDefinition.getName().toLowerCase();
			final IndicesExistsResponse res = client.admin().indices().prepareExists(indexName).execute().actionGet();
			if (!res.isExists()) {
				createIndex(measureDefinition, indexName, client);
			}
			
			final AliasesExistResponse alias = client.admin().indices().prepareAliasesExist(indexName + "-alias").execute().actionGet();
			if (!alias.isExists()) {
				createAlias(indexName, client);
			}
		}
	}

}
