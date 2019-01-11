package org.measure.platform.restapi.application;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.measure.platform.core.api.IApplicationCatalogueService;
import org.measure.platform.service.measurement.impl.ElasticMeasurementStorage;
import org.measure.smm.measurementapplication.model.SMMApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.codahale.metrics.annotation.Timed;

@RestController
@RequestMapping(value = "api/applications")
public class ApplicationResource {
	private final Logger log = LoggerFactory.getLogger(ElasticMeasurementStorage.class);

	@Inject
	IApplicationCatalogueService applicationsCatalogue;





	/**
	 * GET /applications : get all applications.
	 * 
	 * @return the ResponseEntity with status 200 (OK) and the list of applications
	 *         in body
	 */
	@GetMapping("/findall")
	@Timed
	public List<SMMApplication> getAllApplications() {
		List<SMMApplication> result = new ArrayList<>();
		result.addAll(this.applicationsCatalogue.getAllApplications());
		//result.addAll(this.remoteCatalogue.getAllMeasures());
		return result;
	}

}
