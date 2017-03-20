package org.measure.platform.smmengine.api;

import org.measure.platform.core.entity.MeasureInstance;
import org.measure.smm.log.MeasureLog;
import org.measure.smm.measure.api.IMeasure;

public interface IMeasureExecutionService {
	public MeasureLog executeMeasure(MeasureInstance measure,IMeasure measureImpl);
	public MeasureLog testMeasure(Long measureInstanceId);
	public MeasureLog executeMeasure(Long instanceId);
}
