package org.measure.platform.smmengine.impl.logger;

import java.util.ArrayList;
import java.util.List;

import org.measure.platform.smmengine.api.ILoggerService;
import org.measure.smm.log.MeasureLog;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class LoggerService implements ILoggerService{

	private List<MeasureLog> logs = new ArrayList<>();

	@Override
	public List<MeasureLog> getMeasureExecutionLogs() {
		return logs;
	}

	@Override
	public void addMeasureExecutionLog(MeasureLog log) {
		if(logs.size() > 20){
			logs.remove(logs.size()-1);
		}
		logs.add(0,log);
	}

}
