package org.measure.platform.restapi.measure;

import java.util.List;

import javax.inject.Inject;

import org.measure.platform.restapi.measure.dto.MeasurementQuery;
import org.measure.platform.service.measurement.api.IMeasurementStorage;
import org.measure.smm.measure.api.IMeasurement;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

@RestController
@RequestMapping(value = "api/measurement")
public class MeasurementResource {
	
	@Inject
	private IMeasurementStorage measurementStorage;
	

	/**
	 * GET /find find Measurements.
	 * @param : MeasurementQuery : Query
	 * @return the ResponseEntity with status 200 (OK) and the list of projects
	 *         in body
	 */
	@PostMapping("/find")
	@Timed
	public List<IMeasurement> getMeasurements(@RequestBody MeasurementQuery query) {
		return	this.measurementStorage.getMeasurementPage(query.getMeasureInstance(), query.getPageSize(), query.getPage(), query.getQuery());	
	}
}
