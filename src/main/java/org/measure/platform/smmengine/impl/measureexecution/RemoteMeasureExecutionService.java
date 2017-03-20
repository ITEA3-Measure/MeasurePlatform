package org.measure.platform.smmengine.impl.measureexecution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.measure.platform.core.api.entitys.MeasureInstanceService;
import org.measure.platform.core.api.entitys.MeasurePropertyService;
import org.measure.platform.core.entity.MeasureInstance;
import org.measure.platform.core.entity.MeasureProperty;
import org.measure.platform.measurementstorage.api.IMeasurementStorage;
import org.measure.platform.smmengine.api.ILoggerService;
import org.measure.platform.smmengine.api.IRemoteMeasureExecutionService;
import org.measure.platform.smmengine.api.IShedulingService;
import org.measure.smm.log.MeasureLog;
import org.measure.smm.log.MeasureLog.MeasureLogParameters;
import org.measure.smm.measure.api.IMeasurement;
import org.measure.smm.remote.RemoteMeasureInstance;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RemoteMeasureExecutionService  implements IRemoteMeasureExecutionService{

	@Inject
	private MeasurePropertyService measurePropertyService;

	@Inject
	private ILoggerService logger;

	@Inject
	private IMeasurementStorage measurementStorage;

	@Inject
	private IShedulingService shedulingService;
	
	@Inject
	private MeasureInstanceService measureInstanceService;

	@Override
	public void registerRemoteExecution(MeasureLog executionLog) {
		System.err.println("registerRemoteExecution " + executionLog.isSuccess());
		if (executionLog.isSuccess()) {
			
			if(executionLog.getUpdatedParameters().size() > 0){
				MeasureInstance measure = measureInstanceService.findOne(executionLog.getMeasureInstanceId());
				storeUpdatedProperties(measure,executionLog.getUpdatedParameters());
			}

			for (IMeasurement measurement : executionLog.getMesurement()) {
				measurementStorage.putMeasurement(executionLog.getMeasureInstanceName(), true, measurement);
			}
		} else {	
				shedulingService.removeMeasure(executionLog.getMeasureInstanceId());	
		}
		logger.addMeasureExecutionLog(executionLog);
	}

	@Override
	public List<IMeasurement> executeRemoteMeasure(MeasureInstance measure, MeasureLog log, boolean storeProp) {
		RestTemplate restTemplate = new RestTemplate();
		try {

			String url = "http://" + measure.getRemoteAdress() + "/api/measure-agent/measure-execution";

			RemoteMeasureInstance data = new RemoteMeasureInstance();
			data.setInstanceName(measure.getInstanceName());
			data.setMeasureName(measure.getMeasureName());

			Map<String, String> properties = initialiseProperties(measure, null);
			data.setProperties(properties);

			MeasureLog result = restTemplate.postForObject(url, data, MeasureLog.class);

			if (result != null) {
				if (storeProp)
					storeUpdatedProperties(measure, result.getUpdatedParameters());
				log.setLog(result);
				return result.getMesurement();
			} else {
				log.setSuccess(false);
				log.setExceptionMessage("No Result");
			}
		} catch (Exception e) {
			log.setSuccess(false);
			log.setExceptionMessage(e.getMessage());
		}

		return new ArrayList<>();
	}

	private HashMap<String, String> initialiseProperties(MeasureInstance measureData, MeasureLog log) {
		HashMap<String, String> properties = new HashMap<>();
		for (MeasureProperty property : measurePropertyService.findByInstance(measureData)) {
			properties.put(property.getPropertyName(), property.getPropertyValue());
			if (log != null) {
				log.getParameters().add(log.new MeasureLogParameters(property.getPropertyName(), property.getPropertyValue()));
			}
		}
		return properties;
	}

	private void storeUpdatedProperties(MeasureInstance measureData,Map<String,String> updatedProperties) {
		for (MeasureProperty property : new ArrayList<>(measurePropertyService.findByInstance(measureData))) {			
			if(updatedProperties.containsKey(property.getPropertyName())){
				property.setPropertyValue(updatedProperties.get(property.getPropertyName()));				
				measurePropertyService.save(property);
			}
			
		}
	}

}
