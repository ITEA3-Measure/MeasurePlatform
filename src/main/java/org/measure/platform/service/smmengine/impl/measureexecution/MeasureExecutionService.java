package org.measure.platform.service.smmengine.impl.measureexecution;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.measure.platform.core.api.IMeasureCatalogueService;
import org.measure.platform.core.api.entitys.MeasureInstanceService;
import org.measure.platform.core.api.entitys.MeasurePropertyService;
import org.measure.platform.core.api.entitys.MeasureReferenceService;
import org.measure.platform.core.entity.MeasureInstance;
import org.measure.platform.core.entity.MeasureProperty;
import org.measure.platform.core.entity.MeasureReference;
import org.measure.platform.service.measurement.api.IMeasurementStorage;
import org.measure.platform.service.smmengine.api.IMeasureExecutionService;
import org.measure.smm.log.MeasureLog;
import org.measure.smm.measure.api.IDerivedMeasure;
import org.measure.smm.measure.api.IDirectMeasure;
import org.measure.smm.measure.api.IMeasure;
import org.measure.smm.measure.api.IMeasurement;
import org.measure.smm.measure.defaultimpl.measurements.DefaultMeasurement;
import org.springframework.stereotype.Service;

@Service
public class MeasureExecutionService implements IMeasureExecutionService {
	@Inject
	private IMeasureCatalogueService measureCatalogue;

	@Inject
	private MeasureInstanceService measureInstanceService;

	@Inject
	private MeasurePropertyService measurePropertyService;

	@Inject
	private MeasureReferenceService measureReferenceService;

	@Inject
	private IMeasurementStorage measurementStorage;

	@Override
	public MeasureLog executeMeasure(MeasureInstance measureData, IMeasure measureImpl) {
		MeasureLog log = new MeasureLog();
		log.setMeasureInstanceName(measureData.getInstanceName());
		log.setMeasureName(measureData.getMeasureName());

		try {

			List<IMeasurement> measurements = new ArrayList<>();
			measurements.addAll(executeLocalMeasure(measureData, measureImpl, log, true));

			for (IMeasurement measurement : measurements) {
				measurementStorage.putMeasurement(measureData.getMeasureName().toLowerCase(), measureData.getInstanceName(), true, measurement);
			}

			log.setExectionDate(new Date());
		} catch (Exception e) {
			log.setExceptionMessage(e.getMessage());
			log.setSuccess(false);
			e.printStackTrace();
		}
		return log;
	}

	@Override
	public MeasureLog executeMeasure(Long measureInstanceId) {
		MeasureInstance measureData = measureInstanceService.findOne(measureInstanceId);

		MeasureLog log = new MeasureLog();
		log.setExectionDate(new Date());
		log.setMeasureInstanceName(measureData.getInstanceName());
		log.setMeasureName(measureData.getMeasureName());

		try {

			List<IMeasurement> measurements = new ArrayList<>();

			IMeasure measureImpl = measureCatalogue.getMeasureImplementation(measureData.getMeasureName());
			measurements.addAll(executeLocalMeasure(measureData, measureImpl, log, true));

			for (IMeasurement measurement : measurements) {
				measurementStorage.putMeasurement(measureData.getMeasureName().toLowerCase(), measureData.getInstanceName(), true, measurement);
			}

		} catch (NoNodeAvailableException e) {
			log.setExceptionMessage("The Elasticsearch database is not available");
			log.setSuccess(false);
			e.printStackTrace();
		} catch (Throwable e) {
			log.setExceptionMessage(e.getMessage());
			log.setSuccess(false);
			e.printStackTrace();
		}
		return log;
	}
	
	@Override
	public MeasureLog executeMeasure(Long measureInstanceId, Date logDate,String dateField) {
		MeasureInstance measureData = measureInstanceService.findOne(measureInstanceId);

		MeasureLog log = new MeasureLog();
		log.setExectionDate(logDate);
		log.setMeasureInstanceName(measureData.getInstanceName());
		log.setMeasureName(measureData.getMeasureName());

		try {

			List<IMeasurement> measurements = new ArrayList<>();

			IMeasure measureImpl = measureCatalogue.getMeasureImplementation(measureData.getMeasureName());
			measurements.addAll(executeLocalMeasure(measureData, measureImpl, log, true));

			for (IMeasurement measurement : measurements) {
				measurement.getValues().put(dateField, logDate);
				measurementStorage.putMeasurement(measureData.getMeasureName().toLowerCase(), measureData.getInstanceName(), true, measurement);
			}

		} catch (NoNodeAvailableException e) {
			log.setExceptionMessage("The Elasticsearch database is not available");
			log.setSuccess(false);
			e.printStackTrace();
		} catch (Throwable e) {
			log.setExceptionMessage(e.getMessage());
			log.setSuccess(false);
			e.printStackTrace();
		}
		return log;
	}

	@Override
	public MeasureLog testMeasure(Long measureInstanceId) {
		MeasureInstance measureData = measureInstanceService.findOne(measureInstanceId);
		MeasureLog log = new MeasureLog();

		log.setExectionDate(new Date());
		log.setMeasureInstanceName(measureData.getInstanceName());
		log.setMeasureName(measureData.getMeasureName());
		try {

			IMeasure measureImpl = measureCatalogue.getMeasureImplementation(measureData.getMeasureName());
			executeLocalMeasure(measureData, measureImpl, log, false);
		} catch (Throwable e) {
			log.setSuccess(false);
			
			if(e.getMessage() != null){
				log.setExceptionMessage(e.getMessage());
			}else{
				log.setExceptionMessage(ExceptionUtils.getStackTrace(e));
			}
			e.printStackTrace();
		}
		return log;
	}

	private List<IMeasurement> executeLocalMeasure(MeasureInstance measure, IMeasure measureImpl, MeasureLog log, boolean storeProp) throws Exception {

		Map<String, String> properties = initialiseProperties(measure, log);
		for (Entry<String, String> entry : properties.entrySet()) {
			measureImpl.getProperties().put(entry.getKey(), entry.getValue());
		}

		Date start = new Date();
		List<IMeasurement> measurements = new ArrayList<>();
		if (measureImpl instanceof IDirectMeasure) {
			measurements.addAll(executeDirectMeasure((IDirectMeasure) measureImpl));
		} else if (measureImpl instanceof IDerivedMeasure) {

			List<MeasureReference> references = new ArrayList<>();
			for (MeasureReference reference : measureReferenceService.findByInstance(measure)) {
				references.add(reference);
			}
			measurements.addAll(executeDerivedMeasure((IDerivedMeasure) measureImpl, references, log));
		}

		if (storeProp) {
			Map<String, String> updatedProperties = new HashMap<>();
			for (Entry<String, String> entry : measureImpl.getProperties().entrySet()) {
				if (entry.getValue() != null && !entry.getValue().equals(properties.get(entry.getKey()))) {
					updatedProperties.put(entry.getKey(), entry.getValue());
				}
			}
			storeUpdatedProperties(measure, updatedProperties);
		}

		log.setExecutionTime(new Date().getTime() - start.getTime());
		
		
		List<DefaultMeasurement> defaultMeasurements = new ArrayList<>();
		for (IMeasurement measirement : measurements) {
			DefaultMeasurement newDef = new DefaultMeasurement();
		
			for(Entry<String,Object> entry : measirement.getValues().entrySet()){
				newDef.addValue(entry.getKey(), entry.getValue());
			}
			defaultMeasurements.add(newDef);
		}
		
		log.setMesurement(defaultMeasurements);
		log.setSuccess(true);

		return measurements;

	}

	private List<IMeasurement> executeDirectMeasure(IDirectMeasure directMeasure) throws Exception {
		return directMeasure.getMeasurement();
	}

	private List<IMeasurement> executeDerivedMeasure(IDerivedMeasure derivedMeasure, List<MeasureReference> references, MeasureLog log) throws Exception {
		derivedMeasure.cleanMeasureInput();
		for (MeasureReference ref : references) {
			List<IMeasurement> measurements = measurementStorage.getMeasurement(ref.getReferencedInstance().getInstanceName(), ref.getNumberRef(), ref.getFilterExpression());
			for (IMeasurement measurement : measurements) {
				derivedMeasure.addMeasureInput(ref.getReferencedInstance().getInstanceName(), ref.getRole(), measurement);
				log.getInputs().add(log.new MeasureLogInput(ref.getRole(), measurement));
			}
		}

		List<IMeasurement> result = derivedMeasure.calculateMeasurement();

		// Execute Measure
		return result;
	}

	private HashMap<String, String> initialiseProperties(MeasureInstance measureData, MeasureLog log) {
		HashMap<String, String> properties = new HashMap<>();
		for (MeasureProperty property : measurePropertyService.findByInstance(measureData)) {
			properties.put(property.getPropertyName(), property.getPropertyValue());
			if (log != null) {
				if(property.getPropertyType().equals("PASSWORD")){
					log.getParameters().add(log.new MeasureLogParameters(property.getPropertyName(), "**********"));
				}else{
					log.getParameters().add(log.new MeasureLogParameters(property.getPropertyName(), property.getPropertyValue()));

				}
			}
		}
		return properties;
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
