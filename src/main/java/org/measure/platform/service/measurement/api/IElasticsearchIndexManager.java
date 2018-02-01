package org.measure.platform.measurementstorage.api;

import java.util.List;

import org.measure.smm.measure.model.SMMMeasure;

public interface IElasticsearchIndexManager {
    void deleteIndex(SMMMeasure measureDefinition);

    void createIndexWithMapping(SMMMeasure measureDefinition);

	void updateIndex(List<SMMMeasure> measures);
    
    String getBaseMeasureIndex();
    
}
