package org.measure.platform.restapi.app.services;

import java.util.Optional;

import javax.inject.Inject;

import org.measure.platform.core.api.entitys.MeasureInstanceService;
import org.measure.platform.core.entity.MeasureInstance;
import org.measure.platform.smmengine.api.ILoggerService;
import org.measure.platform.smmengine.api.IMeasureExecutionService;
import org.measure.platform.smmengine.api.IShedulingService;
import org.measure.smm.log.MeasureLog;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

@RestController

@RequestMapping(value = "api/measure-instance/sheduling")
public class MeasureExecutionResource {

	@Inject
	private IMeasureExecutionService measureExecutionService;

	@Inject
	private IShedulingService shedulingService;

	@Inject
	private MeasureInstanceService instanceService;
	
	@Inject
	private ILoggerService logger;

	@RequestMapping(value = "/start", method = RequestMethod.GET)
	public Boolean startMeasureSheduling(@RequestParam("id") String id) {
		if (id.matches("\\d+")) {
			Long instanceId = Long.valueOf(id);
			if (!shedulingService.isShedule(instanceId)) {
				MeasureInstance measure = instanceService.findOne(instanceId);
				return shedulingService.scheduleMeasure(measure);
			}
		}
		return null;
	}

	@RequestMapping(value = "/stop", method = RequestMethod.GET)
	public Boolean stopMeasureSheduling(@RequestParam("id") String id) {
		if (id.matches("\\d+")) {
			Long instanceId = Long.valueOf(id);
			if (shedulingService.isShedule(instanceId)) {
				shedulingService.removeMeasure(instanceId);
			}
		}
		return false;
	}


	@RequestMapping(value = "/isshedule", method = RequestMethod.GET)
	public Boolean isSheduledMeasure(@RequestParam("id") String id) {
		if (id.matches("\\d+")) {
			Long instanceId = Long.valueOf(id);
			return shedulingService.isShedule(instanceId);
		}
		return false;
	}

	@Timed
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public ResponseEntity<MeasureLog> testMeasure(@RequestParam("id") String id) {
		if (id.matches("\\d+")) {
			Long instanceId = Long.valueOf(id);
			MeasureLog measurement = measureExecutionService.testMeasure(instanceId);
			return Optional.ofNullable(measurement).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
					.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	
	@Timed
	@RequestMapping(value = "/execute", method = RequestMethod.GET)
	public ResponseEntity<MeasureLog> executeMeasure(@RequestParam("id") String id) {
		if (id.matches("\\d+")) {
			Long instanceId = Long.valueOf(id);
			MeasureLog measurement = measureExecutionService.executeMeasure(instanceId);
			logger.addMeasureExecutionLog(measurement);
			return Optional.ofNullable(measurement).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
					.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

}
