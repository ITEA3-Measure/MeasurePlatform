package org.measure.platform.measurementstorage.api;

import org.measure.smm.measure.model.SMMMeasure;

public interface IElasticsearchIndexManager {
    void deleteIndex(SMMMeasure measureDefinition);

    void createIndexWithMapping(SMMMeasure measureDefinition);

    String getBaseMeasureIndex();


//public void refreshIndex(String index);
}
