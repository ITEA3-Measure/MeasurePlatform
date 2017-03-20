package org.measure.platform.smmengine.api;

import java.util.List;

import org.measure.platform.core.entity.MeasureInstance;
import org.measure.smm.log.MeasureLog;
import org.measure.smm.measure.api.IMeasurement;

public interface IRemoteMeasureExecutionService {

	void registerRemoteExecution(MeasureLog executionLog);

	List<IMeasurement> executeRemoteMeasure(MeasureInstance measure, MeasureLog log, boolean storeProp);

}
