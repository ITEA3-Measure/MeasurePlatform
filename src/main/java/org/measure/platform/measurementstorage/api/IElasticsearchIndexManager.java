package org.measure.platform.measurementstorage.api;

import org.measure.smm.measure.model.SMMMeasure;

public interface IElasticsearchIndexManager {
	public void deleteIndex(SMMMeasure measureDefinition);
	public void createIndexWithMapping(SMMMeasure measureDefinition);
}
