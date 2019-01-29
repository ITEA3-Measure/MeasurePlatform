package org.measure.platform.core.measurement.api;

import java.util.List;

import org.measure.platform.core.data.entity.MeasureInstance;
import org.measure.smm.measure.model.SMMMeasure;

public interface IElasticsearchIndexManager {
    
    void createIndexWithMapping(MeasureInstance measureInstance);
    
    void deleteIndex(MeasureInstance measureInstance);

	void updateIndex(List<SMMMeasure> measures);
    
}
