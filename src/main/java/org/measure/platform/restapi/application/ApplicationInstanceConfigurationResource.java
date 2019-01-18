package org.measure.platform.restapi.application;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.measure.platform.core.entity.Application;
import org.measure.platform.core.entity.MeasureInstance;
import org.measure.platform.restapi.entitys.MeasureInstanceResource;
import org.measure.platform.restapi.framework.rest.util.HeaderUtil;
import org.measure.platform.service.application.api.IApplicationInstanceConfigurationService;
import org.measure.platform.service.application.impl.dto.ApplicationInstanceConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

@RestController
@RequestMapping(value = "api/")
public class ApplicationInstanceConfigurationResource {

	private final Logger log = LoggerFactory.getLogger(MeasureInstanceResource.class);

	@Inject
	private IApplicationInstanceConfigurationService applicationInstanceService;


	@GetMapping("application-instance-configuration/{id}")
	@Timed
	public ResponseEntity<ApplicationInstanceConfiguration> getApplicationConfiguration(@PathVariable Long id) {	

		log.debug("REST request to get ApplicationInstanceConfiguration");      
		ApplicationInstanceConfiguration applicationInstanceConfiguration = applicationInstanceService.getApplicaionInstanceById(id);

        return Optional.ofNullable(applicationInstanceConfiguration)
                .map(result -> new ResponseEntity<>(
                    result,
                    HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@PostMapping("application-instance-configuration")
	@Timed
	public ResponseEntity<ApplicationInstanceConfiguration> createApplicationInstanceConfiguration(@Valid @RequestBody ApplicationInstanceConfiguration applicationInstance) throws URISyntaxException {
		log.debug("REST request createApplicationInstanceConfiguration");      

		ApplicationInstanceConfiguration applicationInstanceConfigurationResult = applicationInstanceService.createApplicaionInstance(applicationInstance);


		return ResponseEntity.created(new URI("/api/application-instance-configuration/" + applicationInstanceConfigurationResult.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("applicationInstance", applicationInstanceConfigurationResult.getId().toString()))
				.body(applicationInstanceConfigurationResult);    	

	}

	@PutMapping("/application-instance-configuration")
	@Timed
	public ResponseEntity<ApplicationInstanceConfiguration> updateApplicationInstanceConfiguration(@Valid @RequestBody ApplicationInstanceConfiguration applicationInstance) throws URISyntaxException {
		
		log.debug("REST request updateApplicationInstanceConfiguration");      

        if (applicationInstance.getId() == null) {
            return createApplicationInstanceConfiguration(applicationInstance);
        }
		ApplicationInstanceConfiguration applicationInstanceConfigurationResult = applicationInstanceService.updateApplicaionInstance(applicationInstance);

        return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert("applicationInstance", applicationInstance.getId().toString()))
                    .body(applicationInstanceConfigurationResult);

	}

	@DeleteMapping("application-instance-configuration/{id}")
	@Timed
	public ResponseEntity<Void> deleteApplicationInstanceConfiguration(@PathVariable Long id) {
        log.debug("REST request to delete application instance : {}", id);     

        applicationInstanceService.deleteApplicaionInstance(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("applicationInstance", id.toString())).build();

	}

}
