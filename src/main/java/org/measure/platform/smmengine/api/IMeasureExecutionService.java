package org.measure.platform.smmengine.api;

import org.measure.platform.core.entity.MeasureInstance;
import org.measure.smm.log.MeasureLog;
import org.measure.smm.measure.api.IMeasure;

public interface IMeasureExecutionService {
    MeasureLog executeMeasure(MeasureInstance measure, IMeasure measureImpl);

    MeasureLog testMeasure(Long measureInstanceId);

    MeasureLog executeMeasure(Long instanceId);

}
