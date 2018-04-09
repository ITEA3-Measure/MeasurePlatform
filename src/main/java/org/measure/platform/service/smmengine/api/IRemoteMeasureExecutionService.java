package org.measure.platform.service.smmengine.api;

import org.measure.smm.log.MeasureLog;

public interface IRemoteMeasureExecutionService {
    void registerRemoteExecution(MeasureLog executionLog);
}
