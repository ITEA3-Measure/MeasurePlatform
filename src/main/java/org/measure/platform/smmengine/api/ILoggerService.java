package org.measure.platform.smmengine.api;

import java.util.List;

import org.measure.smm.log.MeasureLog;


public interface ILoggerService {	
	public List<MeasureLog> getMeasureExecutionLogs();
	public void addMeasureExecutionLog(MeasureLog log);

}
