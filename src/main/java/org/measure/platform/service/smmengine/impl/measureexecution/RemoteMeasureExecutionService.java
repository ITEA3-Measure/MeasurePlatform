package org.measure.platform.service.smmengine.impl.measureexecution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.measure.platform.core.data.api.IMeasureInstanceService;
import org.measure.platform.core.data.api.IMeasurePropertyService;
import org.measure.platform.core.data.entity.MeasureInstance;
import org.measure.platform.core.data.entity.MeasureProperty;
import org.measure.platform.core.measurement.api.IMeasurementStorage;
import org.measure.platform.service.smmengine.api.ILoggerService;
import org.measure.platform.service.smmengine.api.IRemoteMeasureExecutionService;
import org.measure.platform.service.smmengine.api.ISchedulingService;
import org.measure.smm.log.MeasureLog;
import org.measure.smm.measure.api.IMeasurement;
import org.measure.smm.remote.RemoteMeasureInstance;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RemoteMeasureExecutionService implements IRemoteMeasureExecutionService {
	@Inject
	private IMeasurePropertyService measurePropertyService;

	@Inject
	private ILoggerService logger;

	@Inject
	private IMeasurementStorage measurementStorage;

	@Inject
	private ISchedulingService shedulingService;

	@Inject
	private IMeasureInstanceService measureInstanceService;

	@Override
	public void registerRemoteExecution(MeasureLog executionLog) {
		System.err.println("registerRemoteExecution " + executionLog.isSuccess());
		if (executionLog.isSuccess()) {

			if (executionLog.getUpdatedParameters().size() > 0) {
				MeasureInstance measure = measureInstanceService.findOne(executionLog.getMeasureInstanceId());
				storeUpdatedProperties(measure, executionLog.getUpdatedParameters());
			}

			for (IMeasurement measurement : executionLog.getMesurement()) {
				measurementStorage.putMeasurement(executionLog.getMeasureInstanceName(), measurement);
			}
		} else {
			shedulingService.removeMeasure(executionLog.getMeasureInstanceId());
		}
		logger.addMeasureExecutionLog(executionLog);
	}

	private void storeUpdatedProperties(MeasureInstance measureData, Map<String, String> updatedProperties) {
		for (MeasureProperty property : new ArrayList<>(measurePropertyService.findByInstance(measureData))) {
			if (updatedProperties.containsKey(property.getPropertyName())) {
				property.setPropertyValue(updatedProperties.get(property.getPropertyName()));
				measurePropertyService.save(property);
			}

		}
	}

}
