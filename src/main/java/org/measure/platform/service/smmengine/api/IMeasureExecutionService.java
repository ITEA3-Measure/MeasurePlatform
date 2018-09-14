package org.measure.platform.service.smmengine.api;

import java.util.Date;

import org.measure.platform.core.entity.MeasureInstance;
import org.measure.smm.log.MeasureLog;
import org.measure.smm.measure.api.IMeasure;

public interface IMeasureExecutionService {
    MeasureLog executeMeasure(MeasureInstance measure, IMeasure measureImpl);

    MeasureLog testMeasure(Long measureInstanceId);

    MeasureLog executeMeasure(Long instanceId);

	MeasureLog executeMeasure(Long instanceId, Date logDate,String dateField);

}
